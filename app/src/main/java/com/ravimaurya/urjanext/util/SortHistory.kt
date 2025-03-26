package com.ravimaurya.urjanext.util

import android.annotation.SuppressLint
import com.ravimaurya.urjanext.domain.model.HistoryModel
import java.time.LocalDate

import java.time.format.DateTimeFormatterBuilder
import java.time.temporal.ChronoField

val sortBy = listOf("Date", "Amount", "Payment Method", "Status")

enum class SortOptions{
    NULL,
    //Date
    NEWEST,
    OLDEST,
    // Amount
    HIGHEST,
    LOWEST
}

class SortHistory(private val sortOptions: SortOptions){

    @SuppressLint("NewApi")
    private val formatter = DateTimeFormatterBuilder()
        .appendPattern("d MMMM")
        .parseDefaulting(ChronoField.YEAR, 2025)
        .toFormatter()


    @SuppressLint("NewApi")
    fun sort(historyModelList: List<HistoryModel>): List<HistoryModel>{
        return when(sortOptions){
            SortOptions.NULL -> {
                historyModelList
            }
            SortOptions.NEWEST -> {
                historyModelList.sortedByDescending { LocalDate.parse(it.chargingDate, formatter) }
            }
            SortOptions.OLDEST -> {
                 historyModelList.sortedBy { LocalDate.parse(it.chargingDate, formatter) }
            }
            SortOptions.HIGHEST -> {
                historyModelList.sortedByDescending { it.amountPaid }
            }
            SortOptions.LOWEST -> {
                historyModelList.sortedBy { it.amountPaid }
            }
        }

    }



}