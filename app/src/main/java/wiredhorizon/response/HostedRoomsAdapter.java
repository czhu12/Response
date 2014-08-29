package wiredhorizon.response;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import wiredhorizon.response.models.Room;

/**
 * Created by chriszhu on 7/9/14.
 */
public class HostedRoomsAdapter extends ArrayAdapter<Room> {
    public HostedRoomsAdapter(Context context, int resource, List<Room> list) {
        super(context, resource, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;


        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.hosted_room_row, null);
        }
        final Room r = getItem(position);
        TextView tv = (TextView) v.findViewById(R.id.hosted_row_room_name);
        tv.setText(r.getName());

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), RoomHostActivity.class);
                i.putExtra("ROOM_ID", r.getId());
                i.putExtra("ROOM_NAME", r.getName());
                getContext().startActivity(i);
            }
        });

        return v;
    }
}
