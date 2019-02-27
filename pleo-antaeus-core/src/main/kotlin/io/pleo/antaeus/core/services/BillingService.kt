package io.pleo.antaeus.core.services

import io.pleo.antaeus.core.exceptions.CurrencyMismatchException
import io.pleo.antaeus.core.external.PaymentProvider
import io.pleo.antaeus.core.helpers.ExceptionWrapper
import io.pleo.antaeus.core.helpers.IDateTime
import io.pleo.antaeus.core.helpers.Response
import io.pleo.antaeus.data.AntaeusDal
import io.pleo.antaeus.models.Invoice
import io.pleo.antaeus.models.InvoiceStatus
import mu.KotlinLogging


private val logger = KotlinLogging.logger {}
class BillingService(private val dal: AntaeusDal,
                     private val paymentProvider: PaymentProvider) : IDateTime, PaymentProvider, ExceptionWrapper {
   fun run() : Response {
       if(isFirstDayOfTheMonth()) {
           var invoices = getPendingInvoices()
           invoices.forEach { charge(it) }
           var response = Response(statusCode = 200, message = "Successfuly updated ${invoices.count()} invoices", data = dal.fetchInvoices())
           return response
       } else {
           var response = Response(statusCode = 500, message = "Try again next day!", data = emptyList())
           return response
       }
   }

    fun getPendingInvoices() : List<Invoice> {
        return dal.fetchInvoicesByStatus(InvoiceStatus.PENDING)
    }

    fun beginPaymentProcess(invoice: Invoice) : Invoice? {
        return dal.updateInvoiceStatus(invoice = invoice, status = InvoiceStatus.STARTED)
    }

    fun acceptPayment(invoice: Invoice) : Invoice? {
        return dal.updateInvoiceStatus(invoice = invoice, status = InvoiceStatus.PAID)
    }

    fun rejectPayment(invoice: Invoice, error: String) : String {
        dal.updateInvoiceStatus(invoice = invoice, status = InvoiceStatus.REJECTED)
        return error
    }

    override fun charge(invoice: Invoice) : Boolean {
        try {
            beginPaymentProcess(invoice)
            val status = paymentProvider.charge(invoice)
            if (status) acceptPayment(invoice)
            else rejectPayment(invoice = invoice, error = "Probably insufficient balance for ${invoice.customerId}")
            return status
        } catch (e: Exception) {
            raise(e)
            logger.error { "Error occured while charging customer - ${invoice.customerId} because following exception was thrown ${e}" }
            return false
        }
    }
}