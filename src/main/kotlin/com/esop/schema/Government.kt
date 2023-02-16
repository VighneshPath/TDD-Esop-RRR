package com.esop.schema

import jakarta.inject.Singleton
import jdk.jfr.DataAmount
import kotlin.math.roundToLong

const val LESS_THAN_100_TAX_PERCENT_PERF = 0.02
const val LESS_THAN_100_TAX_PERCENT_NON_PERF = 0.01
const val GREATER_THAN_100_TAX_PERCENT_PERF = 0.0225
const val GREATER_THAN_100_TAX_PERCENT_NON_PERF = 0.0125
const val GREATER_THAN_50000_TAX_PERCENT_PERF = 0.025
const val GREATER_THAN_50000_TAX_PERCENT_NON_PERF = 0.015


@Singleton
class Government {
    private var funds: Long = 0



    fun payTax(taxableAmount: Long): Boolean {
        funds+=taxableAmount
        return true
    }

    fun getTaxableAmount(esopType: String, amountPerEsop: Long, quantity: Long): Long {
        var taxableAmount: Long = when (esopType) {
            "PERFORMANCE" -> {
                getTaxForPerformance(amountPerEsop, quantity)
            }

            "NON_PERFORMANCE" -> {
                getTaxForNonPerformance(amountPerEsop, quantity)
            }

            else -> {
                0L
            }
        }
        if(quantity < 50000){
            taxableAmount = reduceToCap(taxableAmount, esopType)
        }
        return taxableAmount
    }

    private fun reduceToCap(taxableAmount: Long, esopType: String): Long {
        when(esopType){
            "NON_PERFORMANCE"->{
                if(taxableAmount > 20){
                    return 20L
                }
            }
            "PERFORMANCE"->{
                if(taxableAmount > 50){
                    return 50L
                }
            }
        }
        return taxableAmount
    }

    private fun getTaxForNonPerformance(amountPerEsop: Long, quantity: Long): Long {
        val taxPercent: Double = when{
            quantity > 50000->{
                GREATER_THAN_50000_TAX_PERCENT_NON_PERF
            }
            quantity in 100..49999 ->{
                GREATER_THAN_100_TAX_PERCENT_NON_PERF
            }
            quantity<100 -> {
                LESS_THAN_100_TAX_PERCENT_NON_PERF
            }

            else -> {0.0}
        }

        return (taxPercent * quantity.toFloat() * amountPerEsop.toFloat()).roundToLong()
    }

    private fun getTaxForPerformance(amountPerEsop: Long, quantity: Long) : Long{
        val taxPercent: Double = when{
            quantity > 50000->{
                GREATER_THAN_50000_TAX_PERCENT_PERF
            }
            quantity in 100..49999 ->{
                GREATER_THAN_100_TAX_PERCENT_PERF
            }
            quantity<100 -> {
                LESS_THAN_100_TAX_PERCENT_PERF
            }

            else -> {0.0}
        }

        return (taxPercent * quantity.toFloat() * amountPerEsop.toFloat()).roundToLong()
    }

    fun getFunds(): Long{
        return funds
    }
}