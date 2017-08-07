package com.example.dyw.testthread;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by dyw on 2017/8/1.
 */

public class NewsAdapter extends BaseAdapter implements AbsListView.OnScrollListener{

    private List<NewsBean> mList;
    private LayoutInflater mInflater;
    private ImageLoader mImageLoader;
    private int mStart,mEnd;
    public static String[] URLS;
    private boolean mFirstIn;

    public NewsAdapter(Context context, List<NewsBean> data, ListView listView){
        mList = data;
        mInflater = LayoutInflater.from(context);
        mImageLoader = new ImageLoader(listView);
        URLS = new String[data.size()];
        for (int i = 0;i<data.size();i++)
        {
            URLS[i] = data.get(i).getNewsIconUrl();
        }
        mFirstIn = true;
        //一定要注册对应的事件
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null)
        {
            viewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_layout,null);
            viewHolder.ivIcon = (ImageView)convertView.findViewById(R.id.iv_icon);


            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            viewHolder.tvContent = (TextView)convertView.findViewById(R.id.tv_content);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.ivIcon.setImageResource(R.mipmap.ic_launcher);

        String url = mList.get(position).newsIconUrl;
        viewHolder.ivIcon.setTag(url);

//        new ImageLoader().showImageByThread(viewHolder.ivIcon,url);
        mImageLoader.showImageByAsyncTask(viewHolder.ivIcon,url);

        viewHolder.tvTitle.setText(mList.get(position).getNewsTitle());
        viewHolder.tvContent.setText(mList.get(position).getNewsContent());

//        System.out.println("mList.get("+position+").getNewsTitle():"+mList.get(position).getNewsTitle());

        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (scrollState == SCROLL_STATE_IDLE)
        {
            //加载可见项
            mImageLoader.loadImages(mStart,mEnd);

        }else {
            //停止任务
            mImageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart = firstVisibleItem;
        mEnd = firstVisibleItem+visibleItemCount;
        //第一次显示的时候调用
        if (mFirstIn && visibleItemCount > 0)
        {
            mImageLoader.loadImages(mStart,mEnd);
            mFirstIn = false;
        }
    }

    class ViewHolder{
        public TextView tvTitle,tvContent;
        public ImageView ivIcon;
    }
}
