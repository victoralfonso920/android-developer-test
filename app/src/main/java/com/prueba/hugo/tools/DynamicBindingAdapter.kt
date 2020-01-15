package com.prueba.hugo.tools

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

class DynamicBindingAdapter<T>(
    private val layout: Int,
    private val entries: List<T>,
    private val action: (vh: DynamicBindingAdapter.ViewHolder, view: ViewDataBinding, entry: T, position: Int) -> Unit): RecyclerView.Adapter<DynamicBindingAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DynamicBindingAdapter.ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                layout,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        action.invoke(holder, holder.view, entries[position], position)
    }

    override fun getItemCount(): Int {
        return entries.size
    }

    class ViewHolder (v: ViewDataBinding) : RecyclerView.ViewHolder(v.root) {
        val view = v
    }





}