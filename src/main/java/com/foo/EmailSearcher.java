package com.foo;

import com.foo.common.EmailDTO;
import com.foo.index.IndexStrategyEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class EmailSearcher {

    public List<EmailDTO> searchEmails(String sender, String titleKeyword, Long dateCreateStart, Long dateCreateEnd) {
        Set<Integer> idSet = null;

        if (sender != null && sender.trim().length() > 0) {
            idSet = IndexStrategyEnum.SENDER_INDEX.search(sender);
        }

        if (titleKeyword != null && titleKeyword.trim().length() > 0) {
            if (idSet != null){
                idSet.retainAll(IndexStrategyEnum.TITLE_KEYWORD_INDEX.search(titleKeyword));
            } else {
                idSet = IndexStrategyEnum.TITLE_KEYWORD_INDEX.search(titleKeyword);
            }
        }

        if (dateCreateStart != null && dateCreateStart > 0) {
            if (idSet != null) {
                idSet.retainAll(IndexStrategyEnum.DATECREATE_INDEX.search(dateCreateStart, null));
            } else {
                idSet = IndexStrategyEnum.DATECREATE_INDEX.search(dateCreateStart, null);
            }
        }

        if (dateCreateEnd != null && dateCreateEnd > 0) {
            if (idSet != null) {
                idSet.retainAll(IndexStrategyEnum.DATECREATE_INDEX.search(null, dateCreateEnd));
            } else {
                idSet = IndexStrategyEnum.DATECREATE_INDEX.search(null, dateCreateEnd);
            }
        }

        List<EmailDTO> res = new ArrayList<>();

        if (idSet != null && idSet.size() > 0) {
            idSet.forEach(item ->
                    res.add(IndexStrategyEnum.getEmailMappedFileInstance().getMappedData().get(item)));
        }
        return res;
    }
}
