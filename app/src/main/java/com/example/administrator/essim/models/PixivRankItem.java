package com.example.administrator.essim.models;

import java.util.List;

/**
 * Created by Administrator on 2018/1/15 0015.
 */

public class PixivRankItem
{
    public List<Response> response;
    public class Response
    {
        public class Works
        {
            public String rank;     //排名

            public String getRank() {
                return rank;
            }

            public void setRank(String rank) {
                this.rank = rank;
            }

            public String getPrevious_rank() {
                return previous_rank;
            }

            public void setPrevious_rank(String previous_rank) {
                this.previous_rank = previous_rank;
            }

            public String previous_rank;    //既往排名

            public class Work
            {
                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String id;
                public String getTitle() {
                    return title;
                }

                public void setTitle(String title) {
                    this.title = title;
                }

                public String title;
                public List<String> tags;

                public class ImageUrls
                {
                    public String getPx_480mw() {
                        return px_480mw;
                    }

                    public void setPx_480mw(String px_480mw) {
                        this.px_480mw = px_480mw;
                    }

                    public String px_480mw;     //图片链接

                    public String getPx_128x128() {
                        return px_128x128;
                    }

                    public void setPx_128x128(String px_128x128) {
                        this.px_128x128 = px_128x128;
                    }

                    public String px_128x128;

                    public String getLarge() {
                        return large;
                    }

                    public void setLarge(String large) {
                        this.large = large;
                    }

                    public String large;
                }

                public String getWidth() {
                    return width;
                }

                public void setWidth(String width) {
                    this.width = width;
                }

                public String getHeight() {
                    return height;
                }

                public void setHeight(String height) {
                    this.height = height;
                }

                public String width;
                public String height;

                public ImageUrls image_urls;

                public User user;

                public class User
                {
                    public String getName() {
                        return name;
                    }

                    public void setName(String name) {
                        this.name = name;
                    }

                    public String name;

                    public String getId() {
                        return id;
                    }

                    public void setId(String id) {
                        this.id = id;
                    }

                    public String id;
                }

                public String getCreated_time() {
                    return created_time;
                }

                public void setCreated_time(String created_time) {
                    this.created_time = created_time;
                }

                public String created_time;

                public Stats stats;

                public class Stats
                {
                    public String getScored_count() {
                        return scored_count;
                    }

                    public void setScored_count(String scored_count) {
                        this.scored_count = scored_count;
                    }

                    public String scored_count;

                    public String getScore() {
                        return score;
                    }

                    public void setScore(String score) {
                        this.score = score;
                    }

                    public String score;

                    public String getViews_count() {
                        return views_count;
                    }

                    public void setViews_count(String views_count) {
                        this.views_count = views_count;
                    }

                    public String views_count;
                }
            }

            public Work work;

        }
        public List<Works> works;
    }
}
