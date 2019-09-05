package com.example.booklist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BookAdapter extends ArrayAdapter<Book> {

    public BookAdapter(@NonNull Context context, int resource, @NonNull List<Book> books) {
        super(context, resource, books);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItemView = convertView;

        if (listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false
            );
        }

        Book currentBook = getItem(position);

        TextView titleTv = listItemView.findViewById(R.id.book_title);
        titleTv.setText(currentBook.getmBookTitle());
        TextView authorTv = listItemView.findViewById(R.id.book_auther);
        authorTv.setText(currentBook.getmAuthor());
        TextView dateTv = listItemView.findViewById(R.id.book_publish_date);
        dateTv.setText(currentBook.getmPublishDate());

        return listItemView;
    }
}
