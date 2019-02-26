package io.pleo.antaeus.core.services

import io.pleo.antaeus.models.Invoice
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}
class BillingService(private val invoiceService: InvoiceService) {
   fun run() : List<Invoice> {
       var pendingInvoices: List<Invoice> = invoiceService.fetchPendingInvoices()
       logger.info { pendingInvoices }
       return pendingInvoices
   }
}