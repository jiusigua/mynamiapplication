package com.charlotte.mynamiapplication;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query;

import static com.charlotte.mynamiapplication.BuildConfig.MY_MOVIE_API_KEY;

/**
 * Created by Administrator on 2017/2/21 0021.
 */
public class MovieMainActivity extends ActionBarActivity {
    private final String LOG_TAG = MovieMainActivity.class.getSimpleName();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_activity_main_);
        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().add(R.id.main_container, new MovieFragment()).commit();
        }
    }

    public static class MovieFragment extends Fragment{
        private MyAdapter myAdapter;
        private int mColumnNum = 2;
        private boolean isPopular = true;
        private  static final String BASE_URL = "http://api.themoviedb.org";

        public MovieFragment(){
        }

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setHasOptionsMenu(true);
        }

        @Override
        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
            inflater.inflate(R.menu.moviemain, menu);
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == R.id.action_setting){
                Intent intent = new Intent(getContext(), MovieSettingActivity.class);
                startActivity(intent);
                return true;
            }

            if (id == R.id.action_refresh){
                updateMovie();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }

        @Override
        public void onStart() {
            super.onStart();
            updateMovie();
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, null);
            RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
            GridLayoutManager mLayoutManager = new GridLayoutManager(getContext(), mColumnNum);
            mRecyclerView.setLayoutManager(mLayoutManager);
            myAdapter = new MyAdapter(getMovies(), getContext());
            mRecyclerView.setAdapter(myAdapter);
            myAdapter.setOnItemClickListener(new MyAdapter.OnRecyclerViewItemClickListener() {
                @Override
                public void onItemClick(View view, MovieResults.ResultsBean data) {
                    if (!isOnline() && data.getRelease_date() == null){
                        Toast.makeText(getActivity(),"无法连接服务器，请查看你的网络连接",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent= new Intent(getActivity(), MovieDetailActivity.class);
                    intent.putExtra("movieObject", data);
                    startActivity(intent);
                }
            });
            return rootView;
        }

        private ArrayList<MovieResults.ResultsBean> getMovies() {
            updateMovie();
            ArrayList<MovieResults.ResultsBean> movies= new ArrayList<>();
            for (int i = 0; i < 4; i++){
                movies.add(new MovieResults.ResultsBean("正在加载中. . .",0.0));
            }
            return movies;
        }

        /**
         * 判断当前网络是否可用
         */
        public boolean isOnline() {
            ConnectivityManager manager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = manager.getActiveNetworkInfo();
            return netInfo != null && netInfo.isConnected();
        }

        /** 电影网络请求URL
         * http://api.themoviedb.org/3/movie/popular?language=zh&api_key=[YOUR_API_KEY]
         * http://api.themoviedb.org/3/movie/top_rated?language=zh&api_key=[YOUR_API_KEY]
        */
        private void updateMovie(){
            if (!isOnline()){
                Toast.makeText(getActivity(),"无法连接网络，请链接网络后重试",Toast.LENGTH_SHORT).show();
                return;
            }
            //创建retrofit对象
            Retrofit retrofit = new Retrofit.Builder()
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl(BASE_URL)
                    .build();
            //创建访问API请求
            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String order = sp.getString(getString(R.string.pref_order_key), getString(R.string.pref_order_popular));
            String language = "zh";
            Call<MovieResults> call = null;
            switch (order){
                case "popular":
                    PopularService popularService = retrofit.create(PopularService.class);
                    call = popularService.getResult(language, MY_MOVIE_API_KEY);
                    break;
                case "top_rated":
                    TopRateService topService = retrofit.create(TopRateService.class);
                    call = topService.getResult(language,  MY_MOVIE_API_KEY);
                    break;
            }
            //发送请求(异步请求)
            call.enqueue(new Callback<MovieResults>() {
                @Override
                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                    //处理结果
                    if (response.isSuccessful()){
                        ArrayList result= (ArrayList) response.body().getResults();
                        myAdapter.setDatas(result);
                        myAdapter.notifyDataSetChanged();
                    }
                }

                @Override
                public void onFailure(Call<MovieResults> call, Throwable throwable) {
                    Log.e("失败",throwable.toString());
                }
            });
        }

        public interface PopularService {
            @GET("/3/movie/popular")
            Call<MovieResults> getResult(@Query("language") String language, @Query("api_key") String api_key);
        }
        public interface TopRateService {
            @GET("/3/movie/top_rated")
            Call<MovieResults> getResult(@Query("language") String language, @Query("api_key") String api_key);
        }
    }

    public static class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener{
        public ArrayList<MovieResults.ResultsBean> movies = null;
        private Context mContext;
        private static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";
        private OnRecyclerViewItemClickListener mOnItemClickListener = null;

        public MyAdapter(ArrayList<MovieResults.ResultsBean> movies, Context context) {
            this.mContext = context;
            this.movies = movies;
        }

        public void setDatas(ArrayList<MovieResults.ResultsBean> movies){
            this.movies=movies;
        }

        //创建新View，被LayoutManager所调用
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.movie_item,viewGroup,false);
            ViewHolder vh = new ViewHolder(view);
            //将创建的View注册点击事件
            view.setOnClickListener( this);
            return vh;
        }

        //将数据与界面进行绑定的操作
        @Override
        public void onBindViewHolder(ViewHolder viewHolder, int position) {
            final MovieResults.ResultsBean movie = movies.get(position);
            viewHolder.mTv_score.setText("评分：" + movie.getVote_average()+"/10");
            viewHolder.mTv_title.setText("片名：" + movie.getTitle());
            //https://image.tmdb.org/t/p/w185/fMlSFgIB4Kr7YmuqNLHEWN2dkBG.jpg
            viewHolder.mIv_pic.setImageResource(R.mipmap.ic_launcher);
            Picasso.with(mContext)
                    .load(IMAGE_BASE_URL+movie.getPoster_path())
                    .placeholder(R.mipmap.ic_launcher)
                    .into(viewHolder.mIv_pic, new com.squareup.picasso.Callback() {
                        @Override
                        public void onSuccess() {
//                            Toast.makeText(mContext,"加载成功",Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(){
//                            Toast.makeText(mContext,"信息暂时无法加载",Toast.LENGTH_SHORT).show();
                        }
                    });
            //将数据保存在itemView的Tag中，以便点击时进行获取
            viewHolder.itemView.setTag(movie);
        }
        //获取数据的数量
        @Override
        public int getItemCount() {
            return movies.size();
        }

        @Override
        public void onClick(View view) {
            if (mOnItemClickListener != null) {
                //注意这里使用getTag方法获取数据
                mOnItemClickListener.onItemClick(view,(MovieResults.ResultsBean)view.getTag());
            }
        }

        //自定义的ViewHolder，持有每个Item的的所有界面元素
        public  class ViewHolder extends RecyclerView.ViewHolder {
            private TextView mTv_score;
            private TextView mTv_title;

            private ImageView mIv_pic;
            public ViewHolder(View view){
                super(view);
                mTv_score = (TextView) view.findViewById(R.id.movie_score);
                mTv_title = (TextView) view.findViewById(R.id.movie_title);
                mIv_pic = (ImageView) view.findViewById(R.id.movie_pic);
            }
        }
        //暴露接口给外面回调
        public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
            this.mOnItemClickListener = listener;
        }

        //define interface
        public static interface OnRecyclerViewItemClickListener {
            void onItemClick(View view , MovieResults.ResultsBean data);
        }
    }
}
