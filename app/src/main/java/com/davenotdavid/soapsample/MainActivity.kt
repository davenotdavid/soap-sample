package com.davenotdavid.soapsample

import android.app.LoaderManager
import android.content.AsyncTaskLoader
import android.content.Loader
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*

/**
 * This sample app requests XML response data (major cities within a given country) from a SOAP web
 * service.
 */
class MainActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<List<String>> {

    // Log tag constant.
    private val LOG_TAG = MainActivity::class.java.simpleName

    // Loader ID constant.
    private val LOADER_ID = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Runs UI initializations/instantiations.
        init()
    }

    private fun init() {

        // Makes the RecyclerView scroll down linearally as well as have a fixed size.
        recycler_view.layoutManager = LinearLayoutManager(this)
        recycler_view.setHasFixedSize(true)

        // Sets the adapter on the RecyclerView so its list can be populated with UI.
        recycler_view.adapter = CitiesAdapter(mutableListOf<String>())

        // Sets the button with the following functionality.
        search_button.setOnClickListener {

            // Initializes/restarts the Loader to begin a background thread for networking purposes.
            loaderManager.restartLoader(LOADER_ID, null, this)

            // Hides the following views if they're not already for new search queries.
            empty_state_textview.visibility = View.INVISIBLE
            recycler_view.visibility = View.INVISIBLE

            // Views the progress bar for a loading UI.
            progress_bar.visibility = View.VISIBLE
        }
    }

    override fun onCreateLoader(i: Int, bundle: Bundle?): Loader<List<String>> {
        return object : AsyncTaskLoader<List<String>>(this) {

            override fun onStartLoading() {
                super.onStartLoading()

                //Log.d(LOG_TAG, "onStartLoading()")

                // Speaks for itself - forces the Loader to load.
                forceLoad()
            }

            override fun loadInBackground(): List<String>? {
                //Log.d(LOG_TAG, "loadInBackground()")

                // Returns null and exits out of this thread immediately should the user not enter
                // any input.
                if (input_query.text.toString().isEmpty()) {
                    return null
                }

                // Returns XML response data via the following Utils object method.
                return QueryUtils.fetchCitiesData(input_query.text.toString())
            }
        }
    }

    override fun onLoadFinished(loader: Loader<List<String>>, responseData: List<String>?) {
        //Log.d(LOG_TAG, "onLoadFinished()")

        // Hides the progress bar after the background thread completes.
        progress_bar.visibility = View.GONE

        // Displays the RecyclerView of data. Otherwise, displays an empty state TextView instead.
        if (responseData != null) {
            recycler_view.visibility = View.VISIBLE
            recycler_view.adapter = CitiesAdapter(responseData)
        } else {
            empty_state_textview.visibility = View.VISIBLE
        }
    }

    override fun onLoaderReset(loader: Loader<List<String>>) {
        //Log.d(LOG_TAG, "onLoaderReset()")

        // "Clears" out the existing data since the loader resetted.
        recycler_view.adapter = CitiesAdapter(mutableListOf<String>())
    }
}
