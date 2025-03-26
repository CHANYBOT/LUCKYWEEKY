package io.ssafy.luckyweeky.schedule.infrastructure.repository;

import io.ssafy.luckyweeky.common.infrastructure.persistence.MyBatisSqlSessionFactory;
import io.ssafy.luckyweeky.schedule.domain.model.MainScheduleEntity;
import io.ssafy.luckyweeky.schedule.domain.model.ScheduleEntity;
import io.ssafy.luckyweeky.schedule.domain.repository.MainScheduleMapper;
import io.ssafy.luckyweeky.schedule.domain.repository.SubScheduleMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScheduleRepository {
    private final SqlSessionFactory sqlSessionFactory;

    public ScheduleRepository() {
        this.sqlSessionFactory = MyBatisSqlSessionFactory.getSqlSessionFactory();
    }

    public boolean insertSchedule(ScheduleEntity scheduleEntity) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            MainScheduleMapper mainScheduleMapper = session.getMapper(MainScheduleMapper.class);
            SubScheduleMapper subScheduleMapper = session.getMapper(SubScheduleMapper.class);
            mainScheduleMapper.insertMainSchedule(scheduleEntity.getMainScheduleEntity());
            scheduleEntity.getSubScheduleEntityList().forEach(subScheduleEntity -> {
                subScheduleMapper.insertSubSchedule(subScheduleEntity);
            });
            session.commit();
            return true;
        }catch (RuntimeException e){
            return false;
        }
    }

    public List<ScheduleEntity> getSchedulesByDateRange(Map<String,Object> params) {
        List<ScheduleEntity> scheduleEntities = new ArrayList<>();
        try (SqlSession session = sqlSessionFactory.openSession()) {
            MainScheduleMapper mainScheduleMapper = session.getMapper(MainScheduleMapper.class);
            SubScheduleMapper subScheduleMapper = session.getMapper(SubScheduleMapper.class);
            List<MainScheduleEntity> mainScheduleEntities = mainScheduleMapper.selectMainSchedulesByDateRange(params);
            mainScheduleEntities.forEach(mainScheduleEntity -> {
                params.put("mainScheduleId",mainScheduleEntity.getMainScheduleId());
                scheduleEntities.add(new ScheduleEntity(
                        mainScheduleEntity,
                        subScheduleMapper.selectSubSchedulesByMainScheduleIdAndDateRange(params)
                ));
            });
            return scheduleEntities;
        }catch (RuntimeException e){
            return scheduleEntities;
        }
    }

    public boolean deleteSchedule(Long scheduleId) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            SubScheduleMapper subScheduleMapper = session.getMapper(SubScheduleMapper.class);
            MainScheduleMapper mainScheduleMapper = session.getMapper(MainScheduleMapper.class);

            subScheduleMapper.deleteSubScheduleByMainScheduleId(scheduleId);

            session.commit();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

    public boolean deleteSubSchedule(String subScheduleTitle) {
        try (SqlSession session = sqlSessionFactory.openSession(false)) {
            SubScheduleMapper subScheduleMapper = session.getMapper(SubScheduleMapper.class);
            subScheduleMapper.deleteSubSchedule(subScheduleTitle);
            session.commit();
            return true;
        } catch (RuntimeException e) {
            return false;
        }
    }

}
