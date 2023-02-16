package com.esop.schema

import com.esop.repository.UserRecords
import com.esop.service.OrderService
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test


class GovernmentTest {
    private lateinit var userRecords: UserRecords
    private lateinit var orderService: OrderService
    private lateinit var government: Government


    @BeforeEach
    fun `It should create user`() {
        userRecords = UserRecords()
        government= Government()
        orderService = OrderService(userRecords, government)

        val buyer1 = User("Sankaranarayanan", "M", "7550276216", "sankaranarayananm@sahaj.ai", "sankar")
        val buyer2 = User("Aditya", "Tiwari", "", "aditya@sahaj.ai", "aditya")
        val seller1 = User("Kajal", "Pawar", "", "kajal@sahaj.ai", "kajal")
        val seller2 = User("Arun", "Murugan", "", "arun@sahaj.ai", "arun")

        userRecords.addUser(buyer1)
        userRecords.addUser(buyer2)
        userRecords.addUser(seller1)
        userRecords.addUser(seller2)
    }
    @Test
    fun `should tax 1 Percentage for non performance sell orders for quantity less than 100 and total amount less than 2000`() {
        //Arrange
        userRecords.getUser("kajal")!!.userNonPerfInventory.addESOPsToInventory(50)
        val sellOrder = Order(10, "SELL", 10, "kajal")
        userRecords.getUser("kajal")!!.userNonPerfInventory.moveESOPsFromFreeToLockedState(10)
        userRecords.getUser("sankar")!!.userWallet.addMoneyToWallet(100)
        val buyOrder = Order(10, "BUY", 10, "sankar")
        userRecords.getUser("sankar")!!.userWallet.moveMoneyFromFreeToLockedState(100)

        // Act
        orderService.placeOrder(sellOrder)
        orderService.placeOrder(buyOrder)

        // Assert
        Assertions.assertEquals(97, userRecords.getUser("kajal")!!.userWallet.getFreeMoney())
        Assertions.assertEquals(0, userRecords.getUser("sankar")!!.userWallet.getFreeMoney())
        Assertions.assertEquals(1, government.getFunds())
    }
}