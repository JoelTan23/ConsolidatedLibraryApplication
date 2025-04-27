
package com.sp.consolidatedlibraryapplication2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class RoomBookingAdapter extends RecyclerView.Adapter<RoomBookingAdapter.RoomBookingViewHolder> {

    private List<RoomBookingModel> roomBookingList;
    private String currentUserId;
    private Context context;
    private DatabaseHelper dbHelper;

   public RoomBookingAdapter(Context context, List<RoomBookingModel> roomBookingList, String currentUserId) {
    this.context = context;
    this.roomBookingList = roomBookingList;
    this.currentUserId = currentUserId;
    this.dbHelper = new DatabaseHelper();
   }

   @NonNull
   @Override
   public RoomBookingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.roomitem, parent, false);
    return new RoomBookingViewHolder(view);
   }

   @Override
   public void onBindViewHolder(@NonNull RoomBookingViewHolder holder, @SuppressLint("RecyclerView") int position) {
    RoomBookingModel booking = roomBookingList.get(position);

    holder.bookingId.setText("Booking ID: " + currentUserId);
    holder.roomType.setText("Room Type: " + booking.getRoomType());
    holder.timeslot.setText("Timeslot: " + booking.getBookingTime());

    // **Only show delete button if booking belongs to current user**
    if (booking.getUserId().equals(currentUserId)) {
     holder.deleteButton.setVisibility(View.VISIBLE);
    } else {
     holder.deleteButton.setVisibility(View.GONE);
    }

    // **Soft Delete Booking (Set user_id to "DELETED")**
       holder.deleteButton.setOnClickListener(v -> {
           dbHelper.deleteRoomBooking(booking.getUserId(), booking.getBookingDate(), booking.getBookingTime(), new DatabaseHelper.OnDataDeletedListener() {

               @Override
               public void onSuccess(String message) {
                   Toast.makeText(v.getContext(), "Booking Deleted!", Toast.LENGTH_SHORT).show();

                   // Remove from list after deletion
                   roomBookingList.remove(position);
                   notifyItemRemoved(position);
                   notifyItemRangeChanged(position, roomBookingList.size());
               }

               @Override
               public void onFailure(String error) {
                   Toast.makeText(v.getContext(), "Error: " + error, Toast.LENGTH_SHORT).show();
               }
           });
       });
   }

   @Override
   public int getItemCount() {
    return (roomBookingList != null) ? roomBookingList.size() : 0;
   }

   public static class RoomBookingViewHolder extends RecyclerView.ViewHolder {
    TextView bookingId, roomType, timeslot;
    Button deleteButton;

    public RoomBookingViewHolder(View itemView) {
     super(itemView);
     bookingId = itemView.findViewById(R.id.booking_id);
     roomType = itemView.findViewById(R.id.room_type);
     timeslot = itemView.findViewById(R.id.timeslot);
     deleteButton = itemView.findViewById(R.id.delete_button);
    }
   }
}

