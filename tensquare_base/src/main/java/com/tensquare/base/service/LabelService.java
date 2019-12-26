package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class LabelService {
    @Autowired
    private LabelDao labelDao;
    @Autowired
    private IdWorker idWorker;

    public List<Label> findAll() {
        return labelDao.findAll();
    }

    public Label findById(String id) {
        return labelDao.findById(id).get();
    }

    public void save(Label label) {
        label.setId(idWorker.nextId() + "");
        labelDao.save(label);
    }

    public void update(Label label) {
        labelDao.save(label);
    }

    public void deleteById(String id) {
        labelDao.deleteById(id);
    }

    public List<Label> findSearch(Label label) {
        return labelDao.findAll((Specification<Label>) (root, query, cb) -> {
            // new一个list集合。来存放所有的条件
            List<Predicate> list = new ArrayList<>();
            if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");// labelname like "%小明%"
                list.add(predicate);
            }
            if (label.getState() != null && !"".equals(label.getState())) {
                Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());// state = "1"
                list.add(predicate);
            }
            // new一个数组作为最终返回值的条件
            Predicate[] parr = new Predicate[list.size()];
            //把list直接转成数组
            parr = list.toArray(parr);
            return cb.and(parr);// where labelname like "%小明%" and state = "1"

        });
    }

    public Page<Label> pageQuery(Label label, int page, int size) {
        // 封装分页对象
        Pageable pageable = PageRequest.of(page - 1, size);
        return labelDao.findAll((Specification<Label>) (root, query, cb) -> {
            List<Predicate> list = new ArrayList<>();
            if (label.getLabelname() != null && !"".equals(label.getLabelname())) {
                Predicate predicate = cb.like(root.get("labelname").as(String.class), "%" + label.getLabelname() + "%");
                list.add(predicate);
            }
            if (label.getState() != null && !"".equals(label.getState())) {
                Predicate predicate = cb.equal(root.get("state").as(String.class), label.getState());
                list.add(predicate);
            }
            Predicate[] parr = new Predicate[list.size()];
            parr = list.toArray(parr);
            return cb.and(parr);
        }, pageable);
    }

    public Page<Label> find(Label label, int page, int size) {
        PageRequest pageable = PageRequest.of(page - 1, size);
        labelDao.findAll((Specification<Label>) (root, query, cb) -> {

            Predicate predicate1 = cb.like(root.get("labelname").as(String.class), label.getLabelname() + "%");

            return predicate1;
        }, pageable);
        return null;
    }
}
