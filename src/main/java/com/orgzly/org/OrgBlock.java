package com.orgzly.org;

public class OrgBlock {
    private final String name;
    private final String content;

    public OrgBlock(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getContent() {
        return content;
    }

//    public static class Builder {
//        private String name;
//        private String content;
//
//        public Builder() {
//        }
//
//        public Builder(String name) {
//            this.setName(name);
//        }
//
//        public Builder(String name, String content) {
//            this.setName(name);
//            this.setContent(content);
//        }
//
//        public Builder setName(String name) {
//            this.name = name;
//            return this;
//        }
//
//        public Builder setContent(String content) {
//            this.content = content;
//            return this;
//        }
//
//        public OrgBlock build() {
//            return new OrgBlock(name, content);
//        }
//    }
}
