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


private const val HIGH_TAX_SLAB = 50000

private const val LOW_TAX_SLAB = 100

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
        if(quantity < HIGH_TAX_SLAB){
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
            quantity > HIGH_TAX_SLAB ->{
                GREATER_THAN_50000_TAX_PERCENT_NON_PERF
            }
            quantity in LOW_TAX_SLAB until HIGH_TAX_SLAB ->{
                GREATER_THAN_100_TAX_PERCENT_NON_PERF
            }
            quantity< LOW_TAX_SLAB -> {
                LESS_THAN_100_TAX_PERCENT_NON_PERF
            }

            else -> {0.0}
        }

        return (taxPercent * quantity.toFloat() * amountPerEsop.toFloat()).roundToLong()
    }

    private fun getTaxForPerformance(amountPerEsop: Long, quantity: Long) : Long{
        val taxPercent: Double = when{
            quantity > HIGH_TAX_SLAB ->{
                GREATER_THAN_50000_TAX_PERCENT_PERF
            }
            quantity in LOW_TAX_SLAB until HIGH_TAX_SLAB ->{
                GREATER_THAN_100_TAX_PERCENT_PERF
            }
            quantity< LOW_TAX_SLAB -> {
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