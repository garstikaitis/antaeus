package io.pleo.antaeus.core.services

import io.mockk.every
import io.mockk.mockk
import io.pleo.antaeus.core.exceptions.InvoiceNotFoundException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Currency
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import io.pleo.antaeus.models.Money
import mu.KotlinLogging
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.math.BigDecimal

class InvoiceServiceTest {
    private val logger = KotlinLogging.logger {}
    private val dal = mockk<AntaeusDal> {
        var money = Money(BigDecimal.TEN, Currency.SEK)
        var pendingInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PENDING)
        var paidInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PAID)
        var listOfInvoices: MutableList<Invoice> = mutableListOf()
        listOfInvoices.add(pendingInvoice)
        listOfInvoices.add(paidInvoice)
        every { fetchInvoice(404) } returns null
        every { updateInvoiceStatus(pendingInvoice, InvoiceStatus.PAID) } returns paidInvoice
        every { fetchInvoicesByStatus(InvoiceStatus.PENDING) } returns listOfInvoices.filter { it.status.equals(InvoiceStatus.PENDING) }
    }

    private val paymentProvider = mockk<PaymentProvider> {
        var money = Money(BigDecimal.TEN, Currency.SEK)
        var pendingInvoice = Invoice(id = 1001, customerId = 20, amount = money, status = InvoiceStatus.PENDING)
        every { charge(invoice = pendingInvoice) } returns true
    }

    private val invoiceService = InvoiceService(dal = dal)

    @Test
    fun `will throw if customer is not found`() {
        assertThrows<InvoiceNotFoundException> {
            invoiceService.fetch(404)
        }
    }

    @Test
    fun `will return only invoices with status 'PENDING'`() {
        var invoices: List<Invoice> = invoiceService.fetchInvoicesByStatus(InvoiceStatus.PENDING)
        assert(invoices.count() == 1)
    }

}