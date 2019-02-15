package com.wildanokt.searchmoviemade.itemclicksupport;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.wildanokt.searchmoviemade.R;

public class MovieItemClickSupport {
    private final RecyclerView mRecyclerView;
    private OnItemClickListener mOnItemClickListener;

    //----------------------------------------------------------
    //item click

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null){
                RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(v);
                mOnItemClickListener.onItemClicked(mRecyclerView, holder.getAdapterPosition(),v);
            }
        }
    };

    //----------------------------------------------------------

    private RecyclerView.OnChildAttachStateChangeListener mAttachListener
            = new RecyclerView.OnChildAttachStateChangeListener() {
        @Override
        public void onChildViewAttachedToWindow(@NonNull View view) {
            if (mOnItemClickListener != null){
                view.setOnClickListener(mOnClickListener);
            }
        }
        @Override
        public void onChildViewDetachedFromWindow(@NonNull View view) {}
    };

    //----------------------------------------------------------

    private MovieItemClickSupport(RecyclerView recyclerView){
        mRecyclerView = recyclerView;
        mRecyclerView.setTag(R.id.item_click_support);
        mRecyclerView.addOnChildAttachStateChangeListener(mAttachListener);
    }

    //----------------------------------------------------------

    public static MovieItemClickSupport addTo(RecyclerView recyclerView){
        MovieItemClickSupport support = (MovieItemClickSupport)recyclerView.getTag(R.id.item_click_support);
        if (support == null){
            support = new MovieItemClickSupport(recyclerView);
        }
        return support;
    }

    //----------------------------------------------------------

    public void setOnItemClickListener(OnItemClickListener listener){
        mOnItemClickListener = listener;
    }

    //----------------------------------------------------------

    public interface OnItemClickListener{
        void onItemClicked(RecyclerView recyclerView, int position, View v);
    }
}
