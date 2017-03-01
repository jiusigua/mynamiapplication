package com.charlotte.mynamiapplication;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Administrator on 2017/2/23 0023.
 */
public class MovieResults implements Parcelable {
    /**
     * "page": 1
     *"results": [{"poster_path": "/AmbtHzH5kGt4dPTw2E4tBZQcLjz.jpg","adult": false,"overview": "故事设定在《美版午夜凶铃2》结尾的13年后，玛蒂尔达·鲁茨（Matilda Lutz）和阿历克斯·罗（Alex Roe）将在片中饰演一对情侣，后者因为看了录像带而开始疏远女友。","release_date": "2017-02-01","genre_ids": [27],"id": 14564,"original_title": "Rings","original_language": "en",
            "title": "午夜凶铃3(美版)","backdrop_path": "/biN2sqExViEh8IYSJrXlNKjpjxx.jpg","popularity": 208.425751,"vote_count": 290,"video": false,"vote_average": 5.1}
     *"total_results": 19697,
     *"total_pages": 985
     */
    private int page;
    private int total_pages;
    private int total_results;
    private List<ResultsBean> results;

    protected MovieResults(Parcel in) {
        page = in.readInt();
        total_pages = in.readInt();
        total_results = in.readInt();
        results = in.createTypedArrayList(ResultsBean.CREATOR);
    }

    public static final Creator<MovieResults> CREATOR = new Creator<MovieResults>() {
        @Override
        public MovieResults createFromParcel(Parcel in) {
            return new MovieResults(in);
        }

        @Override
        public MovieResults[] newArray(int size) {
            return new MovieResults[size];
        }
    };

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(int total_pages) {
        this.total_pages = total_pages;
    }

    public int getTotal_results() {
        return total_results;
    }

    public void setTotal_results(int total_results) {
        this.total_results = total_results;
    }

    public List<ResultsBean> getResults() {
        return results;
    }

    public void setResults(List<ResultsBean> results) {
        this.results = results;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(page);
        dest.writeInt(total_pages);
        dest.writeInt(total_results);
        dest.writeTypedList(results);

    }

    public static class ResultsBean implements Parcelable{
        /**
         * adult : false
         * backdrop_path : /biN2sqExViEh8IYSJrXlNKjpjxx.jpg
         * genre_ids : [27]
         * id : 14564
         * original_language : en
         * original_title : Rings
         * overview : 故事设定在《美版午夜凶铃2》结尾的13年后，玛蒂尔达·鲁茨（Matilda Lutz）和阿历克斯·罗（Alex Roe）将在片中饰演一对情侣，后者因为看了录像带而开始疏远女友。
         * popularity : 159.533213
         * poster_path : /AmbtHzH5kGt4dPTw2E4tBZQcLjz.jpg
         * release_date : 2017-02-01
         * title : 午夜凶铃3(美版)
         * video : false
         * vote_average : 5.1
         * vote_count : 216
         */

        private boolean adult;
        private String backdrop_path;
        private int id;
        private String original_language;
        private String original_title;
        private String overview;
        private double popularity;
        private String poster_path;
        private String release_date;
        private String title;
        private boolean video;
        private double vote_average;
        private int vote_count;
        private List<Integer> genre_ids;

        public ResultsBean(String title, double vote_average) {
            this.title = title;
            this.vote_average = vote_average;
        }

        protected ResultsBean(Parcel in) {
            adult = in.readByte() != 0;
            backdrop_path = in.readString();
            id = in.readInt();
            original_language = in.readString();
            original_title = in.readString();
            overview = in.readString();
            popularity = in.readDouble();
            poster_path = in.readString();
            release_date = in.readString();
            title = in.readString();
            video = in.readByte() != 0;
            vote_average = in.readDouble();
            vote_count = in.readInt();
        }

        public static final Creator<ResultsBean> CREATOR = new Creator<ResultsBean>() {
            @Override
            public ResultsBean createFromParcel(Parcel in) {
                return new ResultsBean(in);
            }

            @Override
            public ResultsBean[] newArray(int size) {
                return new ResultsBean[size];
            }
        };

        public boolean isAdult() {
            return adult;
        }

        public void setAdult(boolean adult) {
            this.adult = adult;
        }

        public String getBackdrop_path() {
            return backdrop_path;
        }

        public void setBackdrop_path(String backdrop_path) {
            this.backdrop_path = backdrop_path;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getOriginal_language() {
            return original_language;
        }

        public void setOriginal_language(String original_language) {
            this.original_language = original_language;
        }

        public String getOriginal_title() {
            return original_title;
        }

        public void setOriginal_title(String original_title) {
            this.original_title = original_title;
        }

        public String getOverview() {
            return overview;
        }

        public void setOverview(String overview) {
            this.overview = overview;
        }

        public double getPopularity() {
            return popularity;
        }

        public void setPopularity(double popularity) {
            this.popularity = popularity;
        }

        public String getPoster_path() {
            return poster_path;
        }

        public void setPoster_path(String poster_path) {
            this.poster_path = poster_path;
        }

        public String getRelease_date() {
            return release_date;
        }

        public void setRelease_date(String release_date) {
            this.release_date = release_date;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public boolean isVideo() {
            return video;
        }

        public void setVideo(boolean video) {
            this.video = video;
        }

        public double getVote_average() {
            return vote_average;
        }

        public void setVote_average(double vote_average) {
            this.vote_average = vote_average;
        }

        public int getVote_count() {
            return vote_count;
        }

        public void setVote_count(int vote_count) {
            this.vote_count = vote_count;
        }

        public List<Integer> getGenre_ids() {
            return genre_ids;
        }

        public void setGenre_ids(List<Integer> genre_ids) {
            this.genre_ids = genre_ids;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {

            dest.writeByte((byte) (adult ? 1 : 0));
            dest.writeString(backdrop_path);
            dest.writeInt(id);
            dest.writeString(original_language);
            dest.writeString(original_title);
            dest.writeString(overview);
            dest.writeDouble(popularity);
            dest.writeString(poster_path);
            dest.writeString(release_date);
            dest.writeString(title);
            dest.writeByte((byte) (video ? 1 : 0));
            dest.writeDouble(vote_average);
            dest.writeInt(vote_count);
        }
    }

}
