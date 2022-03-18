package com.example.voiceassistant.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Message implements Parcelable {
    public String text;
    public Date date;
    public Boolean isSend;

    public Message(String text, Boolean isSend) {
        this.text = text;
        this.isSend = isSend;
        date = new Date();
    }

    protected Message(Parcel in) {
        text = in.readString();
        byte tmpIsSend = in.readByte();
        isSend = tmpIsSend == 0 ? null : tmpIsSend == 1;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(text);
        parcel.writeByte((byte) (isSend == null ? 0 : isSend ? 1 : 2));
    }
}
