import sys
import pandas as pd
import os
import re 
from konlpy.tag import Okt
from collections import Counter 

def extract_word(text):
    hangul = re.compile('[^가-힣]') 
    result = hangul.sub(' ', text) 
    return result

def make_wordlist(diary): 
        words = extract_word(diary)
        okt = Okt()
        words = okt.morphs(words,stem=True)
        words = [x for x in words if len(x)>1]
        words = [x for x in words if x not in stopwords]
        words = " ".join(words)
        return words

movieData = os.getcwd() + "/Recom_movie/prepro_movieList.csv"
movieData = pd.read_csv(movieData,index_col=[0])

with open(os.getcwd() + "/Recom_movie/stopwords.txt", 'r', encoding='ISO-8859-1') as f:
    list_file = f.readlines()

stopwords = list_file[0].split(",")
prepro_diary = make_wordlist(sys.argv[1])

diary = {
    'title' : '내 일기',
    'rate' : 10,
    'description' : prepro_diary
}
movieData = movieData.append(diary, ignore_index=True)

from sklearn.feature_extraction.text import TfidfVectorizer
tfidf = TfidfVectorizer()
tfidf_matrix = tfidf.fit_transform(movieData.description)
from sklearn.metrics.pairwise import linear_kernel
cosine_sim = linear_kernel(tfidf_matrix, tfidf_matrix)
indices = pd.Series(movieData.index, index=movieData.title).drop_duplicates()

def movie_REC(title, cosine_sim=cosine_sim):
    idx = indices[title]
    sim_scores = list(enumerate(cosine_sim[idx]))
    sim_scores = sorted(sim_scores, key=lambda x:x[1], reverse = True)
    sim_scores = sim_scores[1:5]
    movie_indices = [i[0] for i in sim_scores]
    result_df = movieData.iloc[movie_indices].copy()
    result_df = result_df.head(3)
    result_df = result_df['title']
    res = result_df.values
    return res

if __name__ == '__main__':
    res = movie_REC('내 일기')
    for i in res:
        print(i)
