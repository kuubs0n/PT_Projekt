package pt.webscraping;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import pt.webscraping.entities.ListItem;

/**
 * Created by Kuba on 04-Jun-2017.
 */

public class MyRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    Header header;
    List<ListItem> listItems;
    ResultsActivity activity;
    List<VHItem> vhItems;

    public MyRecyclerAdapter(ResultsActivity activity, Header header, List<ListItem> listItems){
        this.activity = activity;
        this.header = header;
        this.listItems = listItems;
        vhItems = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false);

            return new VHHeader(v);
        } else if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_card, parent, false);
            VHItem item = new VHItem(v);

            return new VHItem(v);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    private ListItem getItem(int position) {
        return listItems.get(position);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof VHHeader) {
            VHHeader VHheader = (VHHeader) holder;
            VHheader.txtTitle.setText(header.getHeader());
        } else if (holder instanceof VHItem) {
            ListItem currentItem = getItem(position - 1);
            VHItem VHitem = (VHItem) holder;
            VHitem.txtTitle.setText(currentItem.title);
            VHitem.txtAuthor.setText(currentItem.author);
            VHitem.txtPrice.setText(currentItem.price + " zl.");
            VHitem.txtUrl.setText(currentItem.link);
            final Bitmap[] mIcon_val = {null};
            final URL[] newurl = {null};
            Runnable getPhoto = () -> {
                try {
                    newurl[0] = new URL(getItem(position - 1).photoURL);
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
                VHitem.imageView.setImageBitmap(mIcon_val[0]);
                VHitem.imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            vhItems.add(VHitem);
        }
    }

    @Override
    public int getItemViewType(int position){
        if(isPositionHeader(position))
            return TYPE_HEADER;
        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position)
    {
        return position == 0;
    }

    //increasing getItemcount to 1. This will be the row of header.
    @Override
    public int getItemCount() {
        return listItems.size()+1;
    }

    class VHHeader extends RecyclerView.ViewHolder{
        TextView txtTitle;
        boolean visible = false;
        public VHHeader(View itemView) {
            super(itemView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.txtHeader);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){

                }
            });
        }
    }

    class VHItem extends RecyclerView.ViewHolder{
        TextView txtTitle;
        TextView txtAuthor;
        TextView txtPrice;
        ImageView imageView;
        TextView txtUrl;
        CardView cardView;
        String url;
        public VHItem(View itemView) {
            super(itemView);
            this.txtTitle = (TextView)itemView.findViewById(R.id.textViewTitle);
            this.txtAuthor = (TextView)itemView.findViewById(R.id.textViewAuthor);
            this.txtPrice = (TextView)itemView.findViewById(R.id.textViewPrice);
            this.imageView = (ImageView)itemView.findViewById(R.id.imageBook);
            this.txtUrl = (TextView)itemView.findViewById(R.id.textViewShop);
            this.cardView = (CardView)itemView.findViewById(R.id.cardView);
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    activity.openProductInBrowser(txtUrl.getText().toString());
                }
            });
        }

    }

}
