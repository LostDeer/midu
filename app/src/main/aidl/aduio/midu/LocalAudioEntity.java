package aduio.midu;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ${LostDeer} on 2017/11/15.
 * Github:https://github.com/LostDeer
 */

public class LocalAudioEntity implements Parcelable {
    private String path;
    private String name;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.path);
        dest.writeString(this.name);
    }

    public LocalAudioEntity() {
    }

    protected LocalAudioEntity(Parcel in) {
        this.path = in.readString();
        this.name = in.readString();
    }

    public static final Creator<LocalAudioEntity> CREATOR = new Creator<LocalAudioEntity>() {
        public LocalAudioEntity createFromParcel(Parcel source) {
            return new LocalAudioEntity(source);
        }

        public LocalAudioEntity[] newArray(int size) {
            return new LocalAudioEntity[size];
        }
    };
}
