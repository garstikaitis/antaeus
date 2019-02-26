package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.helpers.IDateTime
import io.pleo.antaeus.core.helpers.Response
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}
class BillingService(private val invoiceService: InvoiceService, private val dal: AntaeusDal) : IDateTime {
   fun run() : Response {
       var invoices = getPendingInvoices()
       invoices.forEach { updateInvoiceStatus(it, InvoiceStatus.PAID) }
       var response = Response(statusCode = 200, message = "Successfuly updated ${pendingInvoices.count()} invoices", data = invoices)
       return response
   }

    fun getPendingInvoices() : List<Invoice> {
        var pendingInvoices: List<Invoice> = invoiceService.fetchInvoicesByStatus(InvoiceStatus.PENDING)
        return pendingInvoices
    }

    fun updateInvoiceStatus(invoice: Invoice, status: InvoiceStatus) : Invoice? {
        return dal.updateInvoiceStatus(invoice, status)
    }
}