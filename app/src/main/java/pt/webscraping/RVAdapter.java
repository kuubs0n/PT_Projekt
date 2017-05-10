package pt.webscraping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import pt.webscraping.entities.ProductView;

/**
 * Created by Kuba on 11-Apr-2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder> {

    CustomItemClickListener listener;

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView title;
        TextView author;
        TextView price;
        TextView shop;
        ImageView imageView;

        BookViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.cardView);
            title = (TextView) itemView.findViewById(R.id.textViewTitle);
            author = (TextView) itemView.findViewById(R.id.textViewAuthor);
            imageView = (ImageView) itemView.findViewById(R.id.imageBook);
            shop = (TextView) itemView.findViewById(R.id.textViewShop);
            price = (TextView) itemView.findViewById(R.id.textViewPrice);
        }
    }

    List<ProductView> products;

    RVAdapter(List<ProductView> products, CustomItemClickListener listener) {
        this.listener = listener;
        this.products = products;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_card, viewGroup, false);
        BookViewHolder bvh = new BookViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, bvh.getPosition());
            }
        });
        return bvh;
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, int i) {
        final Bitmap[] mIcon_val = {null};
        bookViewHolder.title.setText(products.get(i).title);
        bookViewHolder.author.setText(products.get(i).author);
        bookViewHolder.price.setText(products.get(i).price + " zł.");
        final URL[] newurl = {null};
        Runnable getPhoto = () -> {
            try {
                newurl[0] = new URL(products.get(i).photoURL);
                mIcon_val[0] = BitmapFactory.decodeStream(newurl[0].openConnection().getInputStream());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
        Thread get = new Thread(getPhoto);
        get.start();

        try {
            get.join();
            bookViewHolder.imageView.setImageBitmap(mIcon_val[0]);
            bookViewHolder.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
