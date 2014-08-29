package wiredhorizon.response;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import wiredhorizon.response.models.Question;
import wiredhorizon.response.models.Room;

/**
 * Created by chriszhu on 7/10/14.
 */
public class QuestionsAdapter extends ArrayAdapter<Question> {
    public QuestionsAdapter(Context context, int resource, List<Question> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;

        if (v == null) {
            LayoutInflater vi = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.question_row, null);
        }
        TextView questionDisplay = (TextView) v.findViewById(R.id.question_row_display);
        Question entry = getItem(position);
        questionDisplay.setText(entry.getBody());
        return v;
    }
}
