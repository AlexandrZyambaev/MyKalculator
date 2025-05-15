package com.example.mykalculator

import android.util.Log
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.MutableState
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.exp
import kotlin.text.count

fun LogicOfInteraction(text : String, state: MutableState<String>, expression: MutableState<String>){
    //Проверка, какой символ был нажат
    when(text){
        // Очистить
        "AC" -> {
            state.value = ""
            expression.value = ""
        }

        // Стереть последний символ, првоерка, что строка не пустая и не содержит букв
        "◀" -> {
            if (state.value.isNotEmpty() && (!state.value.last().isLetter() || state.value.last() == 'x')) {
                if (state.value.last() == ' '){
                    state.value = state.value.dropLast(3)
                } else {
                    state.value = state.value.dropLast(1)
                }
            } else {
                if (state.value.isEmpty()) {
                    state.value = expression.value
                    expression.value = ""
                }
            }
        }

        "( )" -> {
            if (state.value.isEmpty() && "(" !in state.value){
                state.value += "("
            } else {
                if (!state.value.last().isDigit() && state.value.last() != ')' && state.value.last() != '.'){
                    state.value += "("
                } else {
                    if (state.value.count{it == '('} > state.value.count{it == ')'})
                        state.value += ")"
                }
            }
        }

        // Добавляем знак ".", если строка не пустая и последний символ является числом
        "." -> if(state.value.isNotEmpty() && state.value.last().isDigit())  state.value += "."

        // Добавляем знак "+", если строка не пустая и последний символ является числом
        "+" -> {
            if (state.value.isNotEmpty() && (state.value.last().isDigit() || state.value.last() == ')'))  {
                state.value += " + "
            }
        }

        // Добавляем знак "-", если строка не пустая и последний символ является числом
        "-" -> {
            if (state.value.isEmpty() || state.value.isNotEmpty() && (state.value.last().isDigit() || state.value.last() == ')' || state.value.last() == '(')) {
                state.value += " - "
            }
        }

        // Добавляем знак "х", если строка не пустая и последний символ является числом
        "x" -> {
            if (state.value.isNotEmpty() && (state.value.last().isDigit() || state.value.last() == ')')) {
                state.value += " x "
            }
        }

        // Добавляем знак "÷", если строка не пустая и последний символ является числом
        "÷" -> {
            if (state.value.isNotEmpty() && (state.value.last().isDigit() || state.value.last() == ')'))  {
                state.value += " ÷ "
            }
        }

        // Добавляем знак "%", если строка не пустая и последний символ является числом
        "%" -> {
            if (state.value.isNotEmpty() && (state.value.last().isDigit() || state.value.last() == ')')) {
                state.value += " % "
            }
        }
        // Переходим к вычислению результата, передавая в функцию "calculations" текущее выражение, если строка не пустая и последний символ это число
        "=" -> {
            if (state.value.isNotEmpty() && (state.value.last().isDigit() || state.value.last() == ')') && state.value.count{it == '('} == state.value.count{it == ')'}) {
                expression.value = state.value
                var otvet = removingTheBrackets(state.value)
                // Округляем ответ до 2 знаков после запятой
                var round_otvet = otvet.toDouble()
                round_otvet = BigDecimal(round_otvet).setScale(6, RoundingMode.HALF_UP).toDouble()
                otvet = round_otvet.toString()
                // Проверка, есть ли дробная часть
                if ('.' in otvet) {
                    var countZero = otvet.drop(otvet.indexOf('.'))  // Удаляем целую часть
                    if (countZero.split('0').size - 1 == countZero.length - 1) {    // Проверяем, состоит ли дробная часть только из нулей, если да, то отрезаем ее
                        otvet = otvet.take(otvet.indexOf('.'))
                    }
                }
                state.value = otvet
            }
        }
        // Во всех остальных случаях (ввод цифр) добавляем введенный символ к выражению
        else -> {
            if (state.value.isEmpty()){
                state.value += text
            } else {
                // Если было деление на ноль - ожидаем нажатия АС
                if (state.value != "Infinity" && state.value.last() != ')') {
                    state.value += text
                }
            }
        }
    }
}

fun removingTheBrackets(text : String) : String{
    var otvet = text.replace(" ", "")
    var lastIndxOpen = 0
    var firstIndxClose= 0
    Log.d("MyLog", "Оно работает")
    while ("(" in otvet){
        lastIndxOpen = otvet.lastIndexOf("(")
        firstIndxClose = otvet.indexOf(")", lastIndxOpen)
        var res = otvet.substring(lastIndxOpen + 1, firstIndxClose)
        Log.d("MyLog", "Наивсыший приоритет у - $res")
        res = calculations(res)
        otvet = otvet.take(lastIndxOpen) + res + otvet.drop(firstIndxClose + 1)
        Log.d("MyLog", "Промежуточный ответ $otvet")
    }
    if ("x" in otvet || "÷" in otvet || "+" in otvet || "-" in otvet || "%" in otvet || "(" in otvet){
        otvet = calculations(otvet)
    }
    return otvet
}

fun calculations(text : String) : String{
    var otvet = text
    // Пока есть знаки арифметических операций, подсчитываем ответ согласно приоритету
    while("x" in otvet || "÷" in otvet || "+" in otvet || "-" in otvet || "%" in otvet || "(" in otvet){
        if(otvet[0] == '-' && otvet.count{it == '-'} < 2 && "x" !in otvet && "÷" !in otvet && "+" !in otvet && "%" !in otvet){
            return otvet
        }
        // В первую очередь проверяем операцию "%"
        if ("%" in otvet){
            otvet = dismemberNumber(otvet, otvet.indexOf("%"))
        } else {
            // В вторую очередь проверяем операцию "х"
            if ("x" in otvet) {
                otvet = dismemberNumber(otvet, otvet.indexOf("x"))
            } else {
                // В третью очередь проверяем операцию "÷"
                if ("÷" in otvet) {
                    otvet = dismemberNumber(otvet, otvet.indexOf("÷"))
                } else {
                    // Если ни одного из вышеперечисленных знаков не встретилось, то слева направо подсчитываем ответ выполняя вычитания и сложения
                    var i = 1
                    // Пока символ равен цифре или точке, продолжаем поиск индекаса знака "+" или "-"
                    while(i < otvet.length && (otvet[i].isDigit() || otvet[i] == '.')) {
                        i += 1
                    }
                    otvet = dismemberNumber(otvet, i)
                }
            }
        }
        Log.d("MyLog", "Промежуточный результат $otvet")
        Log.d("MyLog", "---------------------------------------------------------")
    }
    return otvet
}
fun dismemberNumber(text : String, num : Int) : String{
    var otvet = text
    val indx = num   // Индекс места, где находится знак арифметической операции
    var result : Double = 0.0       // Переменная для подсчета результата
    var firstCount = 0              // Кол-во цифр в первом числе
    var lastCount = 0               // Кол-во цифр в втором числе
    var first = ""                  // Первое число
    var last = ""                   // Второе число
    Log.d("MyLog", "Индекс знака ${otvet[indx]} - $indx")
    Log.d("MyLog", "Длина стркои ${otvet.length}")

    // Вычленяем второе число из всего выражения, идем от знака до следующего знака
    // do-while нужен для того, чтобы учесть минус перед числом, т.к. может попасться выражение 25х-1
    var j = indx + 1
    do{
        last += otvet[j]
        lastCount++
        j++
    } while(j < otvet.length && (otvet[j].isDigit() || otvet[j] == '.'))
    Log.d("MyLog", "Второе число $last")
    Log.d("MyLog", "Длина второго числа $lastCount")

    // Вычленяем первое число из всего выражения, идем от знака до предыдущего знака
    j = indx - 1
    while(j >= 0 && (otvet[j].isDigit() || otvet[j] == '.')){
        first += otvet[j]
        firstCount++
        j--
    }
    if (otvet[0] == '-' && j == 0) {
        first += '-'
        firstCount++
    }
    first = first.reversed()    // Разворачиваем первое число, т.к. оно собиралось с конца в начало
    Log.d("MyLog", "Первое число $first")
    Log.d("MyLog", "Длина первого числа $firstCount")

    // Выполняем вычисления в соответствии с принятым знаком
    when(text[indx]){
        '%' -> result = first.toDouble() / 100 * last.toDouble()
        '+' -> result = first.toDouble() + last.toDouble()
        'x' -> result = first.toDouble() * last.toDouble()
        '-' -> result = first.toDouble() - last.toDouble()
        '÷' -> result = first.toDouble() / last.toDouble()
    }

    // Обновляем строку со всем выражением, убирая первое и второе число, а на их место вставляя ответ
    otvet = otvet.take(indx - firstCount) + result.toString() + otvet.drop(indx + lastCount + 1)

    return otvet
}


