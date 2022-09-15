package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.api.getCurrentDate
import com.udacity.asteroidradar.api.getNextDate
import com.udacity.asteroidradar.database.getDatabase
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.repository.AsteroidRepository

class MainFragment : Fragment() {

//    private val viewModel: MainViewModel by lazy {
//        ViewModelProvider(this).get(MainViewModel::class.java)
//    }
    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onViewCreated()"
        }
        //The ViewModelProviders (plural) is deprecated.
        //ViewModelProviders.of(this, DevByteViewModel.Factory(activity.application)).get(DevByteViewModel::class.java)
        ViewModelProvider(this, MainViewModel.Factory(activity.application)).get(MainViewModel::class.java)

    }

    //val viewModel: MainViewModel by activityViewModels()

    private var asteroidAdapter : AsteroidAdapter? = null
    lateinit var binding : FragmentMainBinding



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        asteroidAdapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {asteroid ->

            Log.d("MAIN_FRAg" , "CLICK")

            findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))

        })


        binding.asteroidRecycler.adapter = asteroidAdapter
        setHasOptionsMenu(true)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        viewModel.asteroidData.observe(viewLifecycleOwner, Observer<List<Asteroid>> { asteroid ->
//            asteroid?.apply {
//                asteroidAdapter?.asteroid = asteroid
//            }
//        })

        viewModel.asteroidlist.observe(viewLifecycleOwner, Observer<List<Asteroid>> {
            Log.d("FilterFrag", "observeWEk ${it}")

            it?.apply {
                asteroidAdapter?.asteroid = it
            }
        })




        viewModel.imageData.observe(viewLifecycleOwner, Observer<ImageOfDayResponse> { imageData ->
            imageData?.apply {
                Log.d("MAinImage" , "Test ${imageData.media_type}")
                if (imageData.media_type == "image")
                {
                    Picasso.get().load(imageData.url)
                        .placeholder(R.drawable.placeholder_picture_of_day)
                        .error(R.drawable.placeholder_picture_of_day)
                        .into(binding.activityMainImageOfTheDay)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         val database = getDatabase(requireContext())
         val asteroidRepository = AsteroidRepository(database)
        when (item.itemId) {
            R.id.show_today_menu -> {
                //AsteroidFilterStatus.TODAY
                observeTodayData(asteroidRepository)

            }
            R.id.show_saved_menu -> {
                observeSavedData(asteroidRepository)
               // AsteroidFilterStatus.SAVED
            }
            else -> {
                observeData(asteroidRepository)
                //AsteroidFilterStatus.WEEK
             }
        }











//        viewModel.setFilterValue(
//
//        )



        return true
    }

    private fun observeData(asteroidRepository: AsteroidRepository) {
        asteroidRepository.asteroid.observe(viewLifecycleOwner,
            Observer<List<Asteroid>> {
                Log.d("FilterObseerve", "observeWEk ${it}")

                it?.apply {
                    asteroidAdapter?.asteroid = it
                }
            })
    }
    private fun observeSavedData(asteroidRepository: AsteroidRepository) {
        asteroidRepository.asteroidSaved.observe(viewLifecycleOwner,
            Observer<List<Asteroid>> {
                Log.d("FilterObseerve", "observeWEk ${it}")

                it?.apply {
                    asteroidAdapter?.asteroid = it
                }
            })
    }

    private fun observeTodayData(asteroidRepository: AsteroidRepository) {

        asteroidRepository.asteroidToday.observe(viewLifecycleOwner,
            Observer<List<Asteroid>> {
                Log.d("FilterObseerve", "observeWEk ${it}")

                it?.apply {
                    asteroidAdapter?.asteroid = it
                }
            })
    }
}
