package com.example.kud.data.adapter

import android.content.Context
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.kud.R
import com.example.kud.data.model.CheckOut
import com.example.kud.ui.fragment.DeleteItemCheckOut
import com.example.kud.utils.Constans.IMAGE_OBAT
import kotlinx.android.synthetic.main.item_checkout.view.*
import java.text.NumberFormat
import java.util.*

class AdapterCheck(val context: Context) : RecyclerView.Adapter<AdapterCheck.ViewHolder>() {

    inner class ViewHolder(val item: View) : RecyclerView.ViewHolder(item)

    var listener: DeleteItemCheckOut? = null

    private val differCallback = object : DiffUtil.ItemCallback<CheckOut>() {
        override fun areItemsTheSame(oldItem: CheckOut, newItem: CheckOut): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: CheckOut, newItem: CheckOut): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_checkout, parent, false)
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataPosition = differ.currentList[position]
//        holder.totalPrice.text = "Rp. 22.000"
        with(holder) {

            with(item) {
                with(dataPosition) {
                    totalItems.setText(amountItem.toString())
                    drugName.text = nameItem
                    val totalPrice = NumberFormat.getNumberInstance(Locale.US)
                        .format(priceItem)
                        .replace(",", ".")
                    drugPrice.text = "Rp. $totalPrice"
                    drugType.text = category.toString()
                    imageProduct.load(IMAGE_OBAT + imageItem)

                    val totil = priceItem!! * amountItem!!
                    textView9.text = totil.toString()


                    grantTotal()
                    grandTotalItem()

                    var counter =0
                    btnPlus.setOnClickListener {
                        var amountItem = totalItems.text.toString().toInt()
                        counter++
                        amountItem += counter
                        Toast.makeText(context, "nambah $amountItem ", Toast.LENGTH_SHORT).show()
//                        btnPlus.isEnabled = amountItem <= stockItem!!
//                        btnMinus.isEnabled = amountItem >= 1
//                        val total = amountItem + 1
//                        totalItems.setText(total.toString())
//                        listener?.addItemCheckOut(holder, absoluteAdapterPosition)
                    }
                    totalItems.addTextChangedListener(object : TextWatcher {
                        override fun beforeTextChanged(
                            p0: CharSequence?, p1: Int, p2: Int, p3: Int
                        ) {

                        }

                        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                            grantTotal()

                        }

                        override fun afterTextChanged(qty: Editable?) {
                            val totils = priceItem * qty.toString().toInt()

                            textView9.text = "$totils"

                            var totalItem = 0
                            for (i in 0 until differ.currentList.size) {
                                totalItem += holder.item.textView9.text.toString().toInt()

                            }
//                            Toast.makeText(context, "$totalItem", Toast.LENGTH_SHORT).show()
                            listener?.grandTotalItem(totalItem)
//                            grandTotalItem()


                            var total = 0
                            var totalItems = 0
                            var totalData = 0

                            for (i in 0 until differ.currentList.size) {
                                total += differ.currentList[i].priceItem!!
                                totalItems += differ.currentList[i].amountItem!!
                                totalData = total * totalItems
                            }
                            listener?.grandTotal(totalData)
                        }

                    })

                    btnMinus.setOnClickListener {
                        if (totalItems.text.toString().toInt() != 1) {
                            val amountItem = totalItems.text.toString().toInt()
                            btnMinus.isEnabled = amountItem > 0
                            val total = amountItem - 1
                            totalItems.setText(total.toString())
//                            listener?.addItemCheckOut(holder, absoluteAdapterPosition)
                        }
                    }
                }

                itemView.deleteItem.setOnClickListener {
//                    if (listener != null) {
//                        listener?.deleteItemCheckOut(holder, absoluteAdapterPosition)
////                        Toast.makeText(context, "not null", Toast.LENGTH_SHORT).show()
//                    } else {
////                        Toast.makeText(context, "listener is null", Toast.LENGTH_SHORT).show()
//                    }


                }
            }
        }
    }

    private fun grandTotalItem() {
        var totalItem = 0
        for (i in 0 until differ.currentList.size) {
            totalItem += differ.currentList[i].amountItem!!
        }
        listener?.grandTotalItem(totalItem)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    class OnClickListeners(val clickListener: (checkOut: CheckOut) -> Unit) {
        fun onClick(checkOut: CheckOut) = clickListener(checkOut)
    }

    private fun grantTotal() {
        var total = 0
        var totalItem = 0
        var totalData = 0
        for (i in 0 until differ.currentList.size) {
            total += differ.currentList[i].priceItem!!
            totalItem += differ.currentList[i].amountItem!!
            totalData = total * totalItem
        }
        listener?.grandTotal(total)
    }

    interface OnAdapterListener{
        fun onUpdateTotal()
    }

}

