from datetime import *
from pytz import timezone

import os
import sys
import numpy as np
import pandas as pd
import random


def emotion(emo):
    happy = ['사랑', '설렘', '행복', '연애', '고백', '웃음', '두근두근', '휴식', '평화', 'Like',
             '힐링', '설레임', '여행', '데이트', '카페', '인생', '심쿵', '연인', '미소', '행복한', '트렌디',
             '기분좋은', '밝은', '즐거운', '따뜻한', '재밌는', '기분좋은곡', '밝음', '다잘될거야']
    angry = ['스트레스', '기분전환', '분노', '스트레스해소', '짜증날때', '빡칠때', '짜증', '열받을때', '회사',
             '꿀꿀할때', '가슴이답답할때', '질주', '분노표출', '화날때', '힐링', '기분안좋을때', '긍정적',
             '답답할때', '화', '욕하고싶을때', '퇴사']
    sad = ['눈물', '외로울때', '상처받았을때', '포기하고싶을때', '외롭', '슬픔', '우울', '위로', '힘들때',
           '이유없이슬플때', '힐링', '상처', '잘될거야', '지칠때', '포기하고싶을때', '공허할때']
    fear = ['불안할때', '응원이필요할때', '스트레스', '위로', '잘될거야', '괜찮아', '불면증', '자기전에듣기좋은',
            '편안', '복잡할때', '안정', '잔잔', '지친마음']
    neutral = ['일상', '나른', '여유', '포근', '산뜻', '편안', '평범', '특별', '행복', '설렘', '달달']

    if emo == '행복': 
        result = happy
    elif emo == '화남':
        result = angry
    elif emo == '슬픔':
        result = sad  
    elif emo == '불안':
        result = fear 
    elif emo == '중립':
        result = neutral

    return result

def season():
    spring = ['사랑', '데이트', '달달한', '썸', '벚꽃', '발', '봄', '봄날', '설렘', '봄노래', '산책', '나들이']
    summer = ['시원한', '더위', '청량', '바다', '여름', '청량한', '바캉스', '열대야', '해변', '여름밤']
    autumn = ['산책', '가을밤', '추억', '이별', '낙엽', '쓸쓸', '가을감성', '감성', '발라드']
    winter = ['겨울', '따뜻한', '추움', '따뜻한', '겨울감성', '쌀쌀한', '추위', '12월', '11월', '찬바람', '겨울밤']

    now = datetime.now(timezone('Asia/Seoul'))
    month = now.month
    if month in [12, 1, 2]:
        result = winter
    elif month in [3, 4, 5]:
        result = spring
    elif month in [6, 7, 8]:
        result = summer
    elif month in [9, 10, 11]:
        result = autumn

    return result

def random_tag(emotionplst, season, emo):
    tag_out = []
    if emo in ['happy', 'angry', 'sad', 'fear']:
        tag_out += random.sample(emotionplst, 15)
        tag_out += random.sample(season, 5)

    elif emo == 'neutral':
        tag_out += random.sample(emotionplst, 10)
        tag_out += random.sample(season, 10)

    return tag_out

def run(emo):
  s = season()
  e = emotion(emo)
  total = random_tag(e, s, emo)

  return total

def variable():
    song_meta = pd.read_json(os.getcwd() + '/Recom_song/song_meta.json')
    data = pd.read_json(os.getcwd() + '/Recom_song/data.json')



    # 아티스트 이름, 노래 제목 리스트
    artist = zip(song_meta.artist_name_basket, song_meta.song_name, song_meta.issue_date)

    # 출력부분
    song_id = dict(zip(song_meta["id"], artist))
    conv_song = dict(zip(data["id"], data.songs))

    return data, song_id, conv_song

def sub_recommend(data, total, conv_song):
    # data tag list
    tags = data.tags.tolist()

    len_t = []
    for i, tages in enumerate(tags):
        # 상황에 맞게 나온 'total' tag, 전체 plylist의 tag 중 같은 tag의 수
        len_t.append(len(list(set(tages).intersection(total))))

    # playlist의 id와 중복 tag 수 dict
    ply_tag = dict(zip(data["id"], len_t))
    # 위에 값에서 많이 중복된 playlist 100개 출력
    ply_tag = sorted(ply_tag.items(), key=(lambda x: x[1]), reverse=True)[:10]

    # playlist의 id 추출
    p_id = list(map(lambda x: x[0], ply_tag))

    plylst = []
    return plylst

def recommendtaion(sub_recom, data):
    # 추천된 playlist들중에 상위 10개에서 랜덤 1개 출력
    final_playlist = [(data['id'][x], data['plylst_title'][x], data['tags'][x]) for x in range(20)][1:]
    a = random.sample(final_playlist[:5], 1)
    
    ply_id = a[0][0]
    ply_title = a[0][1]
    ply_tag = a[0][2]

    return ply_id, ply_title, ply_tag

def main(emo):
    data, song_id, conv_song = variable()
    total = run(emo)
    sub_recom = sub_recommend(data, total, conv_song)
    ply_id, ply_title, ply_tag = recommendtaion(sub_recom, data)
    s = conv_song[ply_id]

    song_list = []
    while(1):
      for so in s:
        song_list.append(song_id[so])

      if len(song_list) > 2:
        break  


    choiceLIst = [random.choice(song_list) for i in range(3)]

    return choiceLIst

if __name__ == '__main__':
    res = main(sys.argv[1])
    for i in res:
        print(i[1])