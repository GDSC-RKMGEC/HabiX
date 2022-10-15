package com.plcoding.mvvmtodoapp.ui.health_traacker

import com.plcoding.mvvmtodoapp.ui.expense.Expensescreen

sealed class healthscreen(val route: String){
    object MainScreen: healthscreen(route = "expense_home")
    object SecondaryScreen: healthscreen(route = "second_expense")
}