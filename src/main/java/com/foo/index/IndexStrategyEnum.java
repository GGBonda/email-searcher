package com.foo.index;

import com.foo.EmailMappedFile;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

import static sun.security.krb5.Confounder.longValue;

public enum IndexStrategyEnum {
    SENDER_INDEX {
        @Override
        public Set<Integer> search(Object... param) {
            if (param == null || param.length == 0) return new HashSet<>();
            if (senderIndex.isEmpty()) initIndex();

            return senderIndex.get(String.valueOf(param[0]));
        }

        @Override
        protected void initIndex() {
            if (file.getMappedData().isEmpty()) return;

            file.getMappedData().entrySet().forEach(entry -> {
                String sender = entry.getValue().getSender();

                Set<Integer> value = senderIndex.get(sender);
                if (value == null) value = new HashSet<>();
                value.add(entry.getKey());

                senderIndex.put(sender, value);
            });
        }
    },
    TITLE_KEYWORD_INDEX {
        @Override
        public Set<Integer> search(Object... param) {
            if (param == null || param.length == 0) return new HashSet<>();
            if (titleKeyWordIndex.isEmpty()) initIndex();

            String keyword = String.valueOf(param[0]);
            if (keyword.trim().length() == 0) return new HashSet<>();

            Set<Integer> res = null;
            for (char word : keyword.toCharArray()) {
                Set<Integer> idSet = titleKeyWordIndex.get(word);

                if (res == null) {
                    res = idSet;
                } else {
                    //取交集
                    res.retainAll(idSet);
                }
            }
            return res;
        }

        @Override
        protected void initIndex() {
            if (file.getMappedData().isEmpty()) return;

            file.getMappedData().entrySet().forEach(entry -> {
                String title = entry.getValue().getTitle();

                for (char keyword : title.toCharArray()) {
                    Set<Integer> value = titleKeyWordIndex.get(keyword);

                    if (value == null) value = new HashSet<>();
                    value.add(entry.getKey());

                    titleKeyWordIndex.put(keyword, value);
                }
            });
        }
    },
    DATECREATE_INDEX {
        @Override
        public Set<Integer> search(Object... param) {
            if (param == null || param.length == 0) return new HashSet<>();
            if (dateCreateIndex.isEmpty()) initIndex();

            Long start = (Long)param[0];
            Long end = (Long)param[1];

            SortedMap<Long, Set<Integer>> subMap = null;

            if (start != null && end != null) {
                subMap = dateCreateIndex.subMap(start, end);
            } else if (start != null)  {
                subMap = dateCreateIndex.tailMap(start);
            } else if (end != null) {
                subMap = dateCreateIndex.headMap(end);
            }

            Set<Integer> res = new HashSet<>();
            if (subMap != null)
                subMap.entrySet().forEach(entry -> res.addAll(entry.getValue()));
            return res;
        }

        @Override
        protected void initIndex() {
            if (file.getMappedData().isEmpty()) return;

            file.getMappedData().entrySet().forEach(entry -> {
                Long dateCreate = entry.getValue().getDateCreate();

                Set<Integer> value = dateCreateIndex.get(dateCreate);
                if (value == null) value = new HashSet<>();
                value.add(entry.getKey());

                dateCreateIndex.put(dateCreate, value);
            });
        }
    };

    private static EmailMappedFile file = new EmailMappedFile(IndexStrategyEnum.class.getClassLoader().getResource("email_data.txt"));
    private static Map<String, Set<Integer>> senderIndex = new ConcurrentHashMap<>();
    private static Map<Character, Set<Integer>> titleKeyWordIndex = new ConcurrentHashMap<>();
    private static NavigableMap<Long, Set<Integer>> dateCreateIndex = new ConcurrentSkipListMap<>();

    public static EmailMappedFile getEmailMappedFileInstance() {return file;}
    public abstract Set<Integer> search(Object... param);
    protected abstract void initIndex();
}
