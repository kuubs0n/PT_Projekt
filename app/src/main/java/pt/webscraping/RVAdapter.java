package pt.webscraping;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import pt.webscraping.entities.ProductView;

/**
 * Created by Kuba on 11-Apr-2017.
 */

public class RVAdapter extends RecyclerView.Adapter<RVAdapter.BookViewHolder> {

    CustomItemClickListener listener;

    public static class BookViewHolder extends RecyclerView.ViewHolder{

        CardView cardView;
        TextView title;
        TextView author;
        TextView price;
        ImageView imageView;

        BookViewHolder(View itemView){
            super(itemView);
            cardView = (CardView)itemView.findViewById(R.id.cardView);
            title = (TextView)itemView.findViewById(R.id.textViewTitle);
            author = (TextView)itemView.findViewById(R.id.textViewAuthor);
            imageView = (ImageView)itemView.findViewById(R.id.imageBook);
            price = (TextView)itemView.findViewById(R.id.textViewPrice);
        }
    }

    List<ProductView> products;

    RVAdapter(List<ProductView> products, CustomItemClickListener listener){
        this.products = products;
        this.listener = listener;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public BookViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.book_card, viewGroup, false);
        BookViewHolder bvh = new BookViewHolder(v);
        v.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                listener.onItemClick(v, bvh.getPosition());
            }
        });
        return bvh;
    }

    @Override
    public void onBindViewHolder(BookViewHolder bookViewHolder, int i){
        bookViewHolder.title.setText(products.get(i).title);
        bookViewHolder.author.setText(products.get(i).author);
        bookViewHolder.price.setText(products.get(i).price);
       //bookViewHolder.imageView.setImageResource();
    }

    @Override
    public int getItemCount(){
        return products.size();
    }
}
