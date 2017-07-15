package com.davenotdavid.soapsample

import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

/**
 * Object/singleton class that consists of utility helper methods for making a request for XML
 * response data from a SOAP web service.
 */
object QueryUtils {

    // Elements of a SOAP envelop body.
    private val NAMESPACE = "http://www.webserviceX.NET"
    private val METHOD_NAME = "GetCitiesByCountry"

    // SOAP Action header field URI consisting of the namespace and method that's used to make a
    // call to the web service.
    private val SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME

    // Web service URL (that should be openable) along with the Web Service Definition Language
    // (WSDL) that's used to view the WSDL file by simply adding "?WSDL" to the end of the URL.
    private val URL = "http://www.webservicex.net/globalweather.asmx?WSDL"

    /**
     * Helper method that requests and returns XML response data from a SOAP web service.
     */
    fun fetchResponseData(userInput: String): String? {

        // A simple dynamic object that can be used to build SOAP calls without
        // implementing KvmSerializable. Essentially, this is what goes inside the body of
        // a SOAP envelope - it is the direct subelement of the body and all further sub
        // elements. Instead of this class, custom classes can be used if they implement
        // the KvmSerializable interface.
        val request = SoapObject(NAMESPACE, METHOD_NAME)

        // The following adds a parameter (parameter name, user inputted value).
        request.addProperty("CountryName", userInput)

        // Declares the version of the SOAP request.
        val envelope = SoapSerializationEnvelope(SoapEnvelope.VER12)

        // Set the following variable to true for compatibility with what seems to be the
        // default encoding for .Net-Services.
        envelope.dotNet = true

        // Assigns the SoapObject to the envelope as the outbound message for the SOAP call.
        envelope.setOutputSoapObject(request)

        // A J2SE based HttpTransport layer instantiation of the web service URL and the
        // WSDL file.
        val httpTransport = HttpTransportSE(URL)
        try {

            // This is the actual part that will call the webservice by setting the desired
            // header SOAP Action header field.
            httpTransport.call(SOAP_ACTION, envelope)
        } catch (e: Exception) { // call() can throw either HttpResponseException, IOException, or XmlPullParserException
            e.printStackTrace()
        }

        // Retrieves the SOAP response from the envelope body.
        val response = envelope.bodyIn as SoapObject

        // Only returns a SOAP response body in text should the SOAP result not be null.
        if (response != null) {
            return "SOAP response:\n\n" + response
        }

        // Otherwise, returns null.
        return null
    }
}
