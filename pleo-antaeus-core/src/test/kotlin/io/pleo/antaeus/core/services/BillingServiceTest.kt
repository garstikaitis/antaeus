package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.CustomerNotFoundException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class BillingServiceTest {
    private val dal = mockk<AntaeusDal> {
        every { fetchCustomer(404) } returns null
    }

    private val paymentProvider = mockk<PaymentProvider> {
        var money = Money(BigDecimal.TEN, Currency.SEK)
        var pendingInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PENDING)
        every { charge(invoice = pendingInvoice) } returns true
    }

    private val invoiceService = InvoiceService(dal = dal)

    private val billingService = BillingService(dal = dal, invoiceService = invoiceService, paymentProvider = paymentProvider)

    @Test
    fun `will return true if today is first day of the month`() {
            assert(billingService.isFirstDayOfTheMonth() == false)
    }
}