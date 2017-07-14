package com.davenotdavid.soapsample

import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Loader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import org.ksoap2.SoapEnvelope
import org.ksoap2.serialization.SoapObject
import org.ksoap2.serialization.SoapSerializationEnvelope
import org.ksoap2.transport.HttpTransportSE

import kotlinx.android.synthetic.main.activity_main.*

/**
 * This sample app requests XML response data (major cities within a given country) from a SOAP web
 * service.
 */
class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    // Log tag constant.
    private val LOG_TAG = MainActivity::class.java.simpleName

    // Elements of a SOAP envelop body.
    private val NAMESPACE = "http://www.webserviceX.NET"
    private val METHOD_NAME = "GetCitiesByCountry"

    // SOAP Action header field URI consisting of the namespace and method that's used to make a
    // call to the web service.
    private val SOAP_ACTION = NAMESPACE + "/" + METHOD_NAME

    // Web service URL (that should be openable) along with the Web Service Definition Language
    // (WSDL) that's used to view the WSDL file by simply adding "?WSDL" to the end of the URL.
    private val URL = "http://www.webservicex.net/globalweather.asmx?WSDL"

    // Loader ID constant.
    private val LOADER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Sets the button with the following functionality.
        search_button.setOnClickListener {

            // Initializes/restarts the Loader to begin a background thread for networking purposes.
            loaderManager.restartLoader(LOADER_ID, null, this)

            // Hides the TextView of data for new search queries.
            textview.visibility = View.INVISIBLE

            // Views the progress bar for a loading UI.
            progress_bar.visibility = View.VISIBLE
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<String> {
        return object : AsyncTaskLoader<String>(this) {

            override fun onStartLoading() {
                super.onStartLoading()

                // Speaks for itself - forces the Loader to load.
                forceLoad()
            }

            override fun loadInBackground(): String? {

                // Returns "No Data" text and exits out of this thread immediately.
                if (input_query.text.toString().isEmpty()) {
                    return getString(R.string.no_data_text)
                }

                // A simple dynamic object that can be used to build SOAP calls without
                // implementing KvmSerializable. Essentially, this is what goes inside the body of
                // a SOAP envelope - it is the direct subelement of the body and all further sub
                // elements. Instead of this class, custom classes can be used if they implement
                // the KvmSerializable interface.
                val request = SoapObject(NAMESPACE, METHOD_NAME)

                // The following adds a parameter (parameter, user inputted value).
                request.addProperty("CountryName", input_query.text.toString())

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

                // Otherwise, returns "No Data" text.
                return getString(R.string.no_data_text)
            }
        }
    }

    override fun onLoadFinished(loader: Loader<String>, responseData: String) {

        // Hides the progress bar after the background thread completes.
        progress_bar.visibility = View.GONE

        // Views the TextView of data for new search queries.
        textview.visibility = View.VISIBLE

        // Sets the XML response data as text for the TextView.
        textview.text = responseData
    }

    override fun onLoaderReset(loader: Loader<String>) {}
}
