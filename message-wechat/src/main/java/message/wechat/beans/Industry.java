package message.wechat.beans;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 行业信息.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 16/3/4 下午1:36
 */
public class Industry {
    @JsonProperty("primary_industry")
    private IndustryInfo primary;
    @JsonProperty("secondary_industry")
    private IndustryInfo secondary;

    public IndustryInfo getPrimary() {
        return primary;
    }

    public void setPrimary(IndustryInfo primary) {
        this.primary = primary;
    }

    public IndustryInfo getSecondary() {
        return secondary;
    }

    public void setSecondary(IndustryInfo secondary) {
        this.secondary = secondary;
    }

    public static class IndustryInfo {
        @JsonProperty("first_class")
        private String first;
        @JsonProperty("second_class")
        private String second;

        public String getFirst() {
            return first;
        }

        public void setFirst(String first) {
            this.first = first;
        }

        public String getSecond() {
            return second;
        }

        public void setSecond(String second) {
            this.second = second;
        }
    }
}
