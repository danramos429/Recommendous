/*
Copyright 2015 Joel Parrish

Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the
License. You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
specific language governing permissions and limitations under the License.
 */
package com.staticvillage.recommender.indexer;

import com.staticvillage.recommender.exception.IndexerException;
import org.apache.solr.client.solrj.SolrServerException;

import java.io.IOException;
import java.util.List;

/**
 * Created by joelparrish on 12/28/14.
 */
public interface Indexer<E> {
    public void addBean(E bean) throws IndexerException;
    public void addBeans(List<E> beans) throws IndexerException;
	public boolean updateBean(E bean) throws IndexerException;
    public List<E> getBeans() throws IndexerException;
    public List<E> getBeans(Object obj) throws IndexerException;
}
