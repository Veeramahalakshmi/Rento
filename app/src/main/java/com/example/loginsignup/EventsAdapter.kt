package com.example.loginsignup



import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.imageview.ShapeableImageView
import com.squareup.picasso.Picasso

class EventsAdapter : RecyclerView.Adapter<EventsAdapter.EventViewHolder>() {

    private var eventsList: List<Event> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return EventViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        val currentEvent = eventsList[position]
        holder.bind(currentEvent)
    }

    override fun getItemCount(): Int {
        return eventsList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitList(list: List<Event>) {
        eventsList = list
        notifyDataSetChanged()
    }

    inner class EventViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        private val eventNameTextView: TextView = itemView.findViewById(R.id.eventNameTextView)
        private val ageTextView: TextView = itemView.findViewById(R.id.ageEditText)
        private val phoneNumberTextView : TextView = itemView.findViewById(R.id.phoneNumberEditText)
        private val sportsNameTextView : TextView = itemView.findViewById(R.id.sportsNameEditText)
        private val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        private val sportsCategorySpinner : TextView = itemView.findViewById(R.id.sportCategorySpinner)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val callButton: Button = itemView.findViewById(R.id.callButton)
        private val chatButton: Button = itemView.findViewById(R.id.chatButton)
        val ivItemImage: ShapeableImageView = itemView.findViewById(R.id.ivItemImage)

        init {
            itemView.setOnClickListener(this)
            callButton.setOnClickListener {
                val phoneNumber = eventsList[adapterPosition].phoneNumber
                initiateCall(phoneNumber)
            }

            chatButton.setOnClickListener {
                val phoneNumber = eventsList[adapterPosition].phoneNumber
                initiateChat(phoneNumber)
            }
        }

        override fun onClick(view: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                val currentItem = eventsList[position]
                val intent = Intent(view?.context, Book::class.java)
                intent.putExtra("eventName", currentItem.eventName)
                intent.putExtra("age", currentItem.age)
                intent.putExtra("phoneNumber", currentItem.phoneNumber)
                intent.putExtra("sportsName", currentItem.sportsName)
                intent.putExtra("date", currentItem.date)
                intent.putExtra("sportsCategory", currentItem.sportCategory)
                intent.putExtra("location", currentItem.location)
                intent.putExtra("email", currentItem.email)
                intent.putExtra("imageUrl", currentItem.imageUrl)

                view?.context?.startActivity(intent)
            }
        }

        private fun initiateCall(phoneNumber: String) {
            val intent = Intent(Intent.ACTION_DIAL)
            intent.data = Uri.parse("tel:$phoneNumber")
            itemView.context.startActivity(intent)
        }

        private fun initiateChat(phoneNumber: String) {
            // Format the phone number for WhatsApp
            val formattedPhoneNumber = phoneNumber.replace("\\s".toRegex(), "")

            // Create the intent
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse("https://wa.me/$formattedPhoneNumber")

            // Check if WhatsApp is installed
            if (isAppInstalled("com.whatsapp")) {
                // Open WhatsApp chat
                intent.`package` = "com.whatsapp"
            } else {
                // Open the default SMS app if WhatsApp is not installed
                intent.data = Uri.parse("sms:$formattedPhoneNumber")
            }

            // Start the activity
            itemView.context.startActivity(intent)
        }

        private fun isAppInstalled(packageName: String): Boolean {
            return try {
                itemView.context.packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }
        }

        fun bind(event: Event) {
            eventNameTextView.text = event.eventName
            ageTextView.text = event.age.toString() // Convert age to String if it's not already
            phoneNumberTextView.text = event.phoneNumber
            sportsNameTextView.text = event.sportsName
            sportsCategorySpinner.text = event.sportCategory
            dateTextView.text = event.date
            locationTextView.text = event.location

            if (event.imageUrl.isNotEmpty()) {
                Picasso.get().load(event.imageUrl).into(ivItemImage)
            }
        }
    }
}
