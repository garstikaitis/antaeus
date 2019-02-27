package io.pleo.antaeus.core.services

import io.mockk.Called
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.helpers.Response
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.*
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

private val logger = KotlinLogging.logger {}
class BillingServiceTest {
    private val dal = mockk<AntaeusDal> {
        val customer = Customer(id = 1, currency = Currency.SEK)
        every { fetchCustomer(404) } returns null
        every { fetchCustomer(1) } returns customer
        var money = Money(BigDecimal.TEN, Currency.DKK)
        var pendingInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PENDING)
        var paidInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PAID)
        var listOfInvoices: MutableList<Invoice> = mutableListOf()
        listOfInvoices.add(pendingInvoice)
        listOfInvoices.add(paidInvoice)
        every { fetchInvoicesByStatus(InvoiceStatus.PENDING) } returns listOfInvoices.filter { it.status.equals(InvoiceStatus.PENDING) }
    }

    private val paymentProvider = mockk<PaymentProvider> {
        var money = Money(BigDecimal.TEN, Currency.SEK)
        var pendingInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PENDING)
        every { charge(invoice = pendingInvoice) } returns true
    }

    private val billingService = BillingService(dal = dal, paymentProvider = paymentProvider)


    @Test
    fun `will return true if today is first day of the month`() {
            assert(billingService.isFirstDayOfTheMonth())
    }

    @Test
    fun `will not charge customers if it is not first day of the month`() {
        var testResponse = Response(statusCode = 200, message = "Try again tomorrow", data = emptyList())
        val billingService = mockk<BillingService> {
            every { isFirstDayOfTheMonth() } returns false
            every { run() } returns testResponse
        }
        billingService.isFirstDayOfTheMonth()
        var response = billingService.run()
        assert(response == testResponse)
    }

    @Test
    fun `charge() returns true if paymentProvider accepts the payment`() {
        val money = Money(BigDecimal.TEN, Currency.SEK)
        val pendingInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PENDING)
        val billingService = mockk<BillingService> {
            every { isFirstDayOfTheMonth() } returns true
            every { charge(pendingInvoice) } returns true
        }
        val paymentProvider = mockk<PaymentProvider> {
            every { charge(pendingInvoice) } returns true
        }
        paymentProvider.charge(pendingInvoice)
        val status = billingService.charge(pendingInvoice)
        assert(status)
    }

    @Test
    fun `charge() returns false if paymentProvider rejects the payment`() {
        val money = Money(BigDecimal.TEN, Currency.SEK)
        val pendingInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PENDING)
        val billingService = mockk<BillingService> {
            every { isFirstDayOfTheMonth() } returns true
            every { charge(pendingInvoice) } returns false
        }
        val paymentProvider = mockk<PaymentProvider> {
            every { charge(pendingInvoice) } returns false
        }
        paymentProvider.charge(pendingInvoice)
        val status = billingService.charge(pendingInvoice)
        assert(!status)
    }

    @Test
    fun `should return only pending invoices`() {
        var invoices: List<Invoice> = billingService.getPendingInvoices()
        assert(invoices.count() == 1)
    }



}