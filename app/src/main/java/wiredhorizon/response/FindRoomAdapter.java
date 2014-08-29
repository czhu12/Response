package wiredhorizon.response;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import wiredhorizon.response.models.Room;

/**
 * Created by chriszhu on 7/9/14.
 */
public class FindRoomAdapter extends ArrayAdapter<Room> {
    public FindRoomAdapter(Context context, int resource, List<Room> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.find_room_row, null);
        }

        final Room room = getItem(position);
        TextView tv = (TextView) v.findViewById(R.id.find_row_room_name);
        tv.setText(room.getName());
        Log.i("Requested view", tv.getText().toString());
        v.setTag(R.id.suggested_row_room_id, room.getId());

        return v;
    }
}
