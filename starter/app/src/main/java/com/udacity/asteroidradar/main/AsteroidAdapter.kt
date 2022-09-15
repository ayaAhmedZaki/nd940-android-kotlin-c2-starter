package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.domain.Asteroid
import com.udacity.asteroidradar.databinding.AsteriodItemLayoutBinding

class AsteroidAdapter( val onClickListener: OnClickListener)  :
    RecyclerView.Adapter<AsteroidAdapter.AsteroidViewHolder>() {


    var asteroid: List<Asteroid> = emptyList()
        set(value) {
            field = value
            // For an extra challenge, update this to use the paging library.

            // Notify any registered observers that the data set has changed. This will cause every
            // element in our RecyclerView to be invalidated.
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
//        val withDataBinding: AsteriodItemLayoutBinding = DataBindingUtil.inflate(
//            LayoutInflater.from(parent.context),
//            AsteroidViewHolder.LAYOUT,
//            parent,
//            false)
//        return AsteroidViewHolder(withDataBinding)
        return AsteroidViewHolder(AsteriodItemLayoutBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            onClickListener.onClick(asteroid[position])
        }
        holder.bind(asteroid[position])
    }

    override fun getItemCount() = asteroid.size




    class AsteroidViewHolder(private var binding: AsteriodItemLayoutBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(asteroid: Asteroid) {
            binding.property = asteroid
            // This is important, because it forces the data binding to execute immediately,
            // which allows the RecyclerView to make the correct view size measurements
            binding.executePendingBindings()
        }
    }


    class OnClickListener(val clickListener: (asteroid: Asteroid) -> Unit) {
        fun onClick(asteroid: Asteroid) = clickListener(asteroid)
    }
}