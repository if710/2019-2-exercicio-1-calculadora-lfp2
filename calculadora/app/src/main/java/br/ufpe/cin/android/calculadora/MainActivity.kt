package br.ufpe.cin.android.calculadora

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Recover the state of text_calc and text_info.
        text_calc?.setText(savedInstanceState?.getString("textCalc"))
        text_info.text = savedInstanceState?.getString("textInfo")

        // Set the listener of all the digit numbers buttons
        // to edit the EditText text_cal.
        btn_0.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '0')
        }

        btn_1.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '1')
        }
        btn_2.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '2')
        }
        btn_3.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '3')
        }
        btn_4.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '4')
        }
        btn_5.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '5')
        }
        btn_6.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '6')
        }
        btn_7.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '7')
        }
        btn_8.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '8')
        }
        btn_9.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '9')
        }
        btn_Add.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '+')
        }
        btn_Subtract.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '-')
        }
        btn_Divide.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '/')
        }
        btn_Clear.setOnClickListener {
            text_calc.setText("")
        }
        btn_Dot.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '.')
        }
        btn_LParen.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + '(')
        }
        btn_RParen.setOnClickListener {
            val editTextHello = text_calc.text.toString()
            text_calc.setText(editTextHello + ')')
        }
        btn_Equal.setOnClickListener {
            // Try to fill the text_info with the formula and
            // text_cal with the result. If catch a RuntimeException,
            // raises a toast.
            try {
                val newText = eval(text_calc.text.toString()).toString()
                text_info.setText(text_calc.text.toString())
                text_calc.setText(newText)
            } catch (e: RuntimeException) {
                Toast.makeText(this@MainActivity, e.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString("textInfo", text_info.text.toString())
        outState.putString("textCalc", text_calc.text.toString())
        super.onSaveInstanceState(outState)
    }
    

    //Como usar a função:
    // eval("2+2") == 4.0
    // eval("2+3*4") = 14.0
    // eval("(2+3)*4") = 20.0
    //Fonte: https://stackoverflow.com/a/26227947
    fun eval(str: String): Double {
        return object : Any() {
            var pos = -1
            var ch: Char = ' '
            fun nextChar() {
                val size = str.length
                ch = if ((++pos < size)) str.get(pos) else (-1).toChar()
            }

            fun eat(charToEat: Char): Boolean {
                while (ch == ' ') nextChar()
                if (ch == charToEat) {
                    nextChar()
                    return true
                }
                return false
            }

            fun parse(): Double {
                nextChar()
                val x = parseExpression()
                if (pos < str.length) throw RuntimeException("Caractere inesperado: " + ch)
                return x
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            // | number | functionName factor | factor `^` factor
            fun parseExpression(): Double {
                var x = parseTerm()
                while (true) {
                    if (eat('+'))
                        x += parseTerm() // adição
                    else if (eat('-'))
                        x -= parseTerm() // subtração
                    else
                        return x
                }
            }

            fun parseTerm(): Double {
                var x = parseFactor()
                while (true) {
                    if (eat('*'))
                        x *= parseFactor() // multiplicação
                    else if (eat('/'))
                        x /= parseFactor() // divisão
                    else
                        return x
                }
            }

            fun parseFactor(): Double {
                if (eat('+')) return parseFactor() // + unário
                if (eat('-')) return -parseFactor() // - unário
                var x: Double
                val startPos = this.pos
                if (eat('(')) { // parênteses
                    x = parseExpression()
                    eat(')')
                } else if ((ch in '0'..'9') || ch == '.') { // números
                    while ((ch in '0'..'9') || ch == '.') nextChar()
                    x = java.lang.Double.parseDouble(str.substring(startPos, this.pos))
                } else if (ch in 'a'..'z') { // funções
                    while (ch in 'a'..'z') nextChar()
                    val func = str.substring(startPos, this.pos)
                    x = parseFactor()
                    if (func == "sqrt")
                        x = Math.sqrt(x)
                    else if (func == "sin")
                        x = Math.sin(Math.toRadians(x))
                    else if (func == "cos")
                        x = Math.cos(Math.toRadians(x))
                    else if (func == "tan")
                        x = Math.tan(Math.toRadians(x))
                    else
                        throw RuntimeException("Função desconhecida: " + func)
                } else {
                    throw RuntimeException("Caractere inesperado: " + ch)
                }
                if (eat('^')) x = Math.pow(x, parseFactor()) // potência
                return x
            }
        }.parse()
    }
}
