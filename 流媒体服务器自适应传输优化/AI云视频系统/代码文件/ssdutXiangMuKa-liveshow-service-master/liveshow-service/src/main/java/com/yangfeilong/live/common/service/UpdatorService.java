package com.yangfeilong.live.common.service;

import com.yangfeilong.live.common.model.Updator;

/**
 * Created by gavin on 17-10-3.
 */
public interface UpdatorService {
    Updator findById(Object id);
    boolean deleteById(Object id);
    boolean save(Updator model);
    boolean update(Updator model);
}
