package com.sp.consolidatedlibraryapplication2;

import com.google.gson.annotations.SerializedName;

public class SoftDeleteModel {
    @SerializedName("$set")
    private UpdateFields updateFields;

    public SoftDeleteModel() {
        this.updateFields = new UpdateFields("DELETED"); // Change user_id to "DELETED"
    }

    private static class UpdateFields {
        @SerializedName("user_id")
        private String userId;

        public UpdateFields(String userId) {
            this.userId = userId;
        }
    }
}
