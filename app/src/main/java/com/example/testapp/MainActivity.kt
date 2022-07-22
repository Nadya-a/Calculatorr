package com.example.testapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var canAddOperation = false
    private var canAddDecimal = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun BackSpace(view: View) {
        val str = operation.text.toString()
        if (str.isNotEmpty())
            operation.text = str.substring(0, str.length - 1)
    }

    fun AllClear(view: View) {
        operation.text = ""
        result.text = ""
    }

    fun Equals(view: View) {
        result.text=Calculate()
    }

    fun NumberAction(view: View) {
        if (view is Button) {
            if(view.text=="."){
                if(canAddDecimal)
                    operation.append(view.text)
                canAddDecimal=false
            }
            else
                operation.append(view.text)
            canAddOperation = true
        }
    }

    fun OperatorAction(view: View) {
        if (view is Button && canAddOperation) {
            operation.append(view.text)
            canAddOperation = false
        }
    }

    private fun Calculate(): String {
        val operators = Operators()
        if(operators.isEmpty()) return ""

        val multiplicationAndDivision = multiplicationAndDivisionCalculate(operators)
        if(multiplicationAndDivision.isEmpty()) return ""

        val result = additionAndSubtractionCalculate(multiplicationAndDivision)
        return result.toString()
    }

    private fun additionAndSubtractionCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float
        for(i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+')
                    result += nextDigit
                if (operator == '-')
                    result -= nextDigit
            }
        }
        return result
    }

    private fun multiplicationAndDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains ('*') || list.contains('/'))
        {
            list = calcMultDiv(list)
        }
        return list
    }

    private fun calcMultDiv(passedList: MutableList<Any>): MutableList<Any>{
        val newList = mutableListOf <Any>()
        var restartIndex = passedList.size

        for(i in passedList.indices)
        {
            if (passedList[i] is Char && i != passedList.lastIndex && i< restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Float
                val nextDigit = passedList[i + 1] as Float
                when (operator) {
                    '*' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '/' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }
            if(i > restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun Operators():MutableList<Any>
    {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in operation.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else
            {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }
        if (currentDigit != "")
            list.add(currentDigit.toFloat())
        return list
    }
}



