package com.assignment2

import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.notkamui.keval.Keval
import com.notkamui.keval.KevalZeroDivisionException

class MainActivity : AppCompatActivity(), OnClickListener {
    lateinit var Btn_Number_0: Button
    lateinit var Btn_Number_1: Button
    lateinit var Btn_Number_2: Button
    lateinit var Btn_Number_3: Button
    lateinit var Btn_Number_4: Button
    lateinit var Btn_Number_5: Button
    lateinit var Btn_Number_6: Button
    lateinit var Btn_Number_7: Button
    lateinit var Btn_Number_8: Button
    lateinit var Btn_Number_9: Button

    lateinit var Btn_Backspace: ImageButton
    lateinit var Btn_Clear: Button
    lateinit var Btn_Bracket: Button
    lateinit var Btn_Mod: Button
    lateinit var Btn_Div: Button
    lateinit var Btn_Mul: Button
    lateinit var Btn_Sub: Button
    lateinit var Btn_Add: Button
    lateinit var Btn_Sum: Button
    lateinit var Btn_PlusMin: Button
    lateinit var Btn_Dot: Button

    lateinit var TV_UserInput: TextView
    lateinit var TV_TempResult: TextView

    var Equation: String = ""
    var stateSum: Boolean = true
    var stateBracket: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Numbers
        Btn_Number_0 = findViewById(R.id.btn_0)
        Btn_Number_1 = findViewById(R.id.btn_1)
        Btn_Number_2 = findViewById(R.id.btn_2)
        Btn_Number_3 = findViewById(R.id.btn_3)
        Btn_Number_4 = findViewById(R.id.btn_4)
        Btn_Number_5 = findViewById(R.id.btn_5)
        Btn_Number_6 = findViewById(R.id.btn_6)
        Btn_Number_7 = findViewById(R.id.btn_7)
        Btn_Number_8 = findViewById(R.id.btn_8)
        Btn_Number_9 = findViewById(R.id.btn_9)

        // Symbols
        Btn_Backspace = findViewById(R.id.btn_backspace)
        Btn_Clear = findViewById(R.id.btn_clear)
        Btn_Bracket = findViewById(R.id.btn_bracket)
        Btn_Mod = findViewById(R.id.btn_mod)
        Btn_Div = findViewById(R.id.btn_div)
        Btn_Mul = findViewById(R.id.btn_mul)
        Btn_Sub = findViewById(R.id.btn_sub)
        Btn_Add = findViewById(R.id.btn_add)
        Btn_Sum = findViewById(R.id.btn_sum)
        Btn_PlusMin = findViewById(R.id.btn_plusmin)
        Btn_Dot = findViewById(R.id.btn_dot)

        // Text Views
        TV_UserInput = findViewById(R.id.user_input)
        TV_TempResult = findViewById(R.id.temp_result)

        Btn_Number_0.setOnClickListener(this)
        Btn_Number_1.setOnClickListener(this)
        Btn_Number_2.setOnClickListener(this)
        Btn_Number_3.setOnClickListener(this)
        Btn_Number_4.setOnClickListener(this)
        Btn_Number_5.setOnClickListener(this)
        Btn_Number_6.setOnClickListener(this)
        Btn_Number_7.setOnClickListener(this)
        Btn_Number_8.setOnClickListener(this)
        Btn_Number_9.setOnClickListener(this)

        Btn_Backspace.setOnClickListener(this)
        Btn_Clear.setOnClickListener(this)
        Btn_Bracket.setOnClickListener(this)
        Btn_Mod.setOnClickListener(this)
        Btn_Div.setOnClickListener(this)
        Btn_Mul.setOnClickListener(this)
        Btn_Sub.setOnClickListener(this)
        Btn_Add.setOnClickListener(this)
        Btn_Sum.setOnClickListener(this)
        Btn_PlusMin.setOnClickListener(this)
        Btn_Dot.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        if (!stateSum) {
            Equation = ""
            stateSum = true
        }

        when (v?.id) {
            R.id.btn_clear -> {
                Equation = ""
                stateSum = true
                stateBracket = false
                _updateUserInput(Equation)
            }

            R.id.btn_backspace -> {
                Equation =
                    if (Equation.length > 1) Equation.removeRange(
                        Equation.length - 1,
                        Equation.length
                    )
                    else ""
                _updateUserInput(Equation)
            }

            R.id.btn_0 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("0"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_1 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("1"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_2 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("2"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_3 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("3"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_4 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("4"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_5 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("5"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_6 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("6"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_7 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("7"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_8 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("8"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_9 -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("9"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_mod -> {
                if (Equation.length < 11 && Equation.isNotEmpty()) {
                    Equation = _checkIfValidEq(Equation.plus("%"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_div -> {
                if (Equation.length < 11 && Equation.isNotEmpty()) {
                    Equation = _checkIfValidEq(Equation.plus("/"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_mul -> {
                if (Equation.length < 11 && Equation.isNotEmpty()) {
                    Equation = _checkIfValidEq(Equation.plus("*"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_sub -> {
                if (Equation.length < 11 && Equation.isNotEmpty()) {
                    Equation = _checkIfValidEq(Equation.plus("-"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_add -> {
                if (Equation.length < 11 && Equation.isNotEmpty()) {
                    Equation = _checkIfValidEq(Equation.plus("+"))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_dot -> {
                if (Equation.length < 11) {
                    Equation = _checkIfValidEq(Equation.plus("."))
                    _updateUserInput(Equation)
                }
            }

            R.id.btn_sum -> {
                if (Equation.isNotEmpty()) {
                    stateSum = false
                    try {
                        _updateUserInput(_sumEq(Equation))
                    } catch (e: Exception) {
                        if (e is KevalZeroDivisionException) {
                            _updateUserInput("Zero Divisor Error!")
                            val toast = Toast.makeText(
                                this,
                                "Can't be divided by zero",
                                Toast.LENGTH_LONG)
                            toast.show()
                        }
                        else {
                            _updateUserInput(e.message!!)
                            val toast = Toast.makeText(
                                this,
                                e.message!!,
                                Toast.LENGTH_LONG
                            )
                            toast.show()
                        }
                    }
                }
            }

            R.id.btn_bracket -> {
                if (stateBracket) {

                    Equation = Equation.plus(")")
                    _updateUserInput(Equation)
                    stateBracket = false
                }
                else {
                    if (Equation.isEmpty() || _isSymbol(Equation.last())) {
                        Equation = Equation.plus("(")
                        _updateUserInput(Equation)
                        stateBracket = true
                    }
                }
            }

            R.id.btn_plusmin -> {
                if (Equation.isEmpty() || _isSymbol(Equation.last())) {
                    Equation = Equation.plus("(-")
                    _updateUserInput(Equation)
                    stateBracket = true
                }
            }
        }
    }

    private fun _sumEq(Eq: String): String {
        try {
            return Keval.eval(Eq).toString()
        } catch (e: Exception) {
            throw e
        }
    }

    private fun _updateUserInput(Eq: String) {
        TV_UserInput.text = Eq
        try {
            TV_TempResult.text = (Keval.eval(Eq).toString())
        } catch (e: Exception) {
            TV_TempResult.text = ""
        }
    }

    private fun _checkIfValidEq(Eq: String): String {
        if (Eq.length < 2) return Eq

        if (_isSymbol(Eq.elementAt(Eq.length - 2)) &&
            _isSymbol(Eq.last())
        ) {
            return Eq.removeRange(Eq.length - 2, Eq.length - 1)
        }
        return Eq
    }

    private fun _isSymbol(c: Char): Boolean {
        return c == '/' ||
                c == '*' ||
                c == '-' ||
                c == '+'
    }
}
