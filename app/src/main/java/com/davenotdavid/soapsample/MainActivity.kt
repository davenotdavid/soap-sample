package com.davenotdavid.soapsample

import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Loader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*

/**
 * This sample app requests XML response data (major cities within a given country) from a SOAP web
 * service.
 */
class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<String> {

    // Log tag constant.
    private val LOG_TAG = MainActivity::class.java.simpleName

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

                // Returns null and exits out of this thread immediately should the user not enter
                // any input.
                if (input_query.text.toString().isEmpty()) {
                    return null
                }

                // Returns XML response data via the following Utils object method.
                return QueryUtils.fetchResponseData(input_query.text.toString())
            }
        }
    }

    override fun onLoadFinished(loader: Loader<String>, responseData: String?) {

        // Hides the progress bar after the background thread completes.
        progress_bar.visibility = View.GONE

        // Views the TextView of data for new search queries.
        textview.visibility = View.VISIBLE

        // Sets the XML response data as text for the TextView. Otherwise, sets "No Data" as text.
        if (responseData != null) {
            textview.text = responseData
        } else {
            textview.text = getString(R.string.no_data_text)
        }
    }

    override fun onLoaderReset(loader: Loader<String>) {}
}
