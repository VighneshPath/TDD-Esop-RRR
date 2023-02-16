package com.esop.schema

import com.esop.exceptions.AmountLessThanZeroException
import com.esop.exceptions.QuantityLessThanZeroException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.roundToLong


class GovernmentTest {
    private lateinit var government: Government


    @BeforeEach
    fun `It should setup government`() {
        government = Government()
    }

    @Test
    fun `should charge 1 percent for quantity less than first slab for NON_PERFORMANCE and below cap`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 10
        val esopType = "NON_PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals((amountPerEsop * quantity * 0.01).roundToLong(), taxableAmount)
    }

    @Test
    fun `should charge 1 percent for quantity less than first slab for NON_PERFORMANCE and exceeding cap`() {
        val amountPerEsop: Long = 10000
        val quantity: Long = 10
        val esopType = "NON_PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(20L, taxableAmount)
    }

    @Test
    fun `should exceed cap for 2nd tax bracket for NON_PERFORMANCE`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 1000
        val esopType = "NON_PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(20L, taxableAmount)
    }

    @Test
    fun `should not exceed cap for 2nd tax bracket for NON_PERFORMANCE`() {
        val amountPerEsop: Long = 1
        val quantity: Long = 1000
        val esopType = "NON_PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(13L, taxableAmount)
    }


    @Test
    fun `should charge 1dot5 for quantity greater than third slab for NON_PERFORMANCE`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 500045
        val esopType = "NON_PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(
            (GREATER_THAN_50000_TAX_PERCENT_NON_PERF * quantity * amountPerEsop).roundToLong(),
            taxableAmount
        )
    }

    @Test
    fun `should charge 1 percent for quantity less than first slab for PERFORMANCE and below cap`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 10
        val esopType = "PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(
            (amountPerEsop * quantity * LESS_THAN_100_TAX_PERCENT_PERF).roundToLong(),
            taxableAmount
        )
    }

    @Test
    fun `should charge 1 percent for quantity less than first slab for PERFORMANCE and exceeding cap`() {
        val amountPerEsop: Long = 10000
        val quantity: Long = 10
        val esopType = "PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(50L, taxableAmount)
    }

    @Test
    fun `should exceed cap for 2nd tax bracket for PERFORMANCE`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 1000
        val esopType = "PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(50L, taxableAmount)
    }

    @Test
    fun `should not exceed cap for 2nd tax bracket for PERFORMANCE`() {
        val amountPerEsop: Long = 1
        val quantity: Long = 1000
        val esopType = "PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(
            (quantity * amountPerEsop * GREATER_THAN_100_TAX_PERCENT_PERF).roundToLong(),
            taxableAmount
        )
    }


    @Test
    fun `should charge 1dot5 for quantity greater than third slab for PERFORMANCE`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 500045
        val esopType = "PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(
            (GREATER_THAN_50000_TAX_PERCENT_PERF * quantity * amountPerEsop).roundToLong(),
            taxableAmount
        )
    }

    @Test
    fun `should throw an exception if quantity is non positive`(){
        val amountPerEsop = 10L
        val quantity = -1L
        val esopType = "NON_PERFORMANCE"

        assertThrows(QuantityLessThanZeroException::class.java){
            government.getTaxableAmount(esopType, amountPerEsop, quantity)
        }
    }

    @Test
    fun `should throw an exception if amount is non positive`(){
        val amountPerEsop = -1L
        val quantity = 1L
        val esopType = "NON_PERFORMANCE"

        assertThrows(AmountLessThanZeroException::class.java){
            government.getTaxableAmount(esopType, amountPerEsop, quantity)
        }
    }

}