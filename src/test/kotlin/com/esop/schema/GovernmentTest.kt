package com.esop.schema

import com.esop.repository.UserRecords
import com.esop.service.OrderService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.math.roundToLong


class GovernmentTest {
    private lateinit var government: Government


    @BeforeEach
    fun `It should setup government`() {
        government= Government()
    }
    @Test
    fun `should tax 1 Percentage for non performance sell orders for quantity less than 100 and total amount less than 2000`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 10
        val esopType ="NON_PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals((amountPerEsop*quantity*0.01).roundToLong(), taxableAmount)
    }
    @Test
    fun `should exceed cap for 2nd tax bracket`() {
        val amountPerEsop: Long = 10
        val quantity: Long = 1000
        val esopType ="NON_PERFORMANCE"

        val taxableAmount = government.getTaxableAmount(esopType, amountPerEsop, quantity)

        Assertions.assertEquals(20L, taxableAmount)
    }


}