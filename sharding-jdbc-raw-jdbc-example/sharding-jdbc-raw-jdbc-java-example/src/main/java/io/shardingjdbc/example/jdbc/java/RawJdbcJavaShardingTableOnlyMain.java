/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package io.shardingjdbc.example.jdbc.java;

import io.shardingjdbc.core.api.ShardingDataSourceFactory;
import io.shardingjdbc.core.api.config.ShardingRuleConfiguration;
import io.shardingjdbc.core.api.config.TableRuleConfiguration;
import io.shardingjdbc.core.api.config.strategy.StandardShardingStrategyConfiguration;
import io.shardingjdbc.example.jdbc.java.algorithm.ModuloShardingDatabaseAlgorithm;
import io.shardingjdbc.example.jdbc.java.repository.RawJdbcRepository;
import io.shardingjdbc.example.jdbc.java.util.DataSourceUtil;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public final class RawJdbcJavaShardingTableOnlyMain {

    // CHECKSTYLE:OFF
    public static void main(final String[] args) throws SQLException {
        // CHECKSTYLE:ON
        new RawJdbcRepository(getShardingDataSource()).demo();
    }

    private static DataSource getShardingDataSource() throws SQLException {
        ShardingRuleConfiguration shardingRuleConfig = new ShardingRuleConfiguration();
        shardingRuleConfig.getTableRuleConfigs().add(getOrderTableRuleConfiguration());
        shardingRuleConfig.getTableRuleConfigs().add(getOrderItemTableRuleConfiguration());
        shardingRuleConfig.getBindingTableGroups().add("t_order, t_order_item");

        //设置默认的table分片规则
        shardingRuleConfig.setDefaultTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id", ModuloShardingDatabaseAlgorithm.class.getName()));

        Properties props = new Properties();
        props.put("sql.show", true);
        return ShardingDataSourceFactory.createDataSource(createDataSourceMap(), shardingRuleConfig, new HashMap<String, Object>(), props);
    }

    private static TableRuleConfiguration getOrderTableRuleConfiguration() {
        TableRuleConfiguration orderTableRuleConfig = new TableRuleConfiguration();
        orderTableRuleConfig.setLogicTable("t_order");
        orderTableRuleConfig.setActualDataNodes("demo_ds.t_order_${[0, 1]}");
        orderTableRuleConfig.setKeyGeneratorColumnName("order_id");
        //设置t_order表的分片规则
        //orderTableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id", ModuloShardingDatabaseAlgorithm.class.getName()));
        return orderTableRuleConfig;
    }

    private static TableRuleConfiguration getOrderItemTableRuleConfiguration() {
        TableRuleConfiguration orderItemTableRuleConfig = new TableRuleConfiguration();
        orderItemTableRuleConfig.setLogicTable("t_order_item");
        orderItemTableRuleConfig.setActualDataNodes("demo_ds.t_order_item_${[0, 1]}");
        //设置t_order_item表的分片规则
        //orderItemTableRuleConfig.setTableShardingStrategyConfig(new StandardShardingStrategyConfiguration("user_id", ModuloShardingDatabaseAlgorithm.class.getName()));
        return orderItemTableRuleConfig;
    }

    private static Map<String, DataSource> createDataSourceMap() {
        Map<String, DataSource> result = new HashMap<>(1, 1);
        result.put("demo_ds", DataSourceUtil.createDataSource("demo_ds"));
        return result;
    }
}
