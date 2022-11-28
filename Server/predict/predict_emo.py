# Setting parameters
max_len = 64
batch_size = 64

import os
import sys
import numpy as np
import gluonnlp as nlp
import torch
from torch import nn
from torch.utils.data import Dataset
import gluonnlp as nlp
import numpy as np
import pickle
#kobert
from kobert.utils import get_tokenizer

import kss
from hanspell import spell_checker

#CPU 사용
device = torch.device("cpu")
class BERTDataset(Dataset):
    def __init__(self, dataset, sent_idx, label_idx, bert_tokenizer, max_len,
                 pad, pair):
        transform = nlp.data.BERTSentenceTransform(
            bert_tokenizer, max_seq_length=max_len, pad=pad, pair=pair)

        self.sentences = [transform([i[sent_idx]]) for i in dataset]
        self.labels = [np.int32(i[label_idx]) for i in dataset]

    def __getitem__(self, i):
        return (self.sentences[i] + (self.labels[i], ))

    def __len__(self):
        return (len(self.labels))

class BERTClassifier(nn.Module):
    def __init__(self,
                 bert,
                 hidden_size = 768,
                 num_classes=7,   ##클래스 수 조정##
                 dr_rate=None,
                 params=None):
        super(BERTClassifier, self).__init__()
        self.bert = bert
        self.dr_rate = dr_rate
                 
        self.classifier = nn.Linear(hidden_size , num_classes)
        if dr_rate:
            self.dropout = nn.Dropout(p=dr_rate)
    
    def gen_attention_mask(self, token_ids, valid_length):
        attention_mask = torch.zeros_like(token_ids)
        for i, v in enumerate(valid_length):
            attention_mask[i][:v] = 1
        return attention_mask.float()

    def forward(self, token_ids, valid_length, segment_ids):
        attention_mask = self.gen_attention_mask(token_ids, valid_length)
        
        _, pooler = self.bert(input_ids = token_ids, token_type_ids = segment_ids.long(), attention_mask = attention_mask.float().to(token_ids.device))
        if self.dr_rate:
            out = self.dropout(pooler)
        return self.classifier(out)

def loadModel():
    #load model
    modelPath = os.getcwd() + "/predict/model.pt"
    model = torch.load(modelPath, map_location=device)
    vocapPath = os.getcwd() + "/predict//vocab.p"
    with open(vocapPath, 'rb') as file:
      vocab = pickle.load(file)
    tokenizer = get_tokenizer(os.getcwd() + "/predict/.cache")

    tok = nlp.data.BERTSPTokenizer(tokenizer, vocab, lower=False)
    return model, tok

def predict(predict_sentence, model, tok):
    data = [predict_sentence, '0']
    dataset_another = [data]

    another_test = BERTDataset(dataset_another, 0, 1, tok, max_len, True, False)
    test_dataloader = torch.utils.data.DataLoader(another_test, batch_size=batch_size, num_workers=5)
    model.eval()

    for batch_id, (token_ids, valid_length, segment_ids, label) in enumerate(test_dataloader):
        token_ids = token_ids.long().to(device)
        segment_ids = segment_ids.long().to(device)

        valid_length= valid_length
        label = label.long().to(device)

        out = model(token_ids, valid_length, segment_ids)

        for i in out:
            logits = i
            logits = logits.detach().cpu().numpy()

            if np.argmax(logits) == 0:
              print(0)
            elif np.argmax(logits) == 1:
              print(1)
            elif np.argmax(logits) == 2:
              print(2)
            elif np.argmax(logits) == 3:
              print(3)
            elif np.argmax(logits) == 4:
              print(4)

def seperate():
    sentence = sys.argv[1]
    spelled_sent = spell_checker.check(sentence)
    hanspell_sent = spelled_sent.checked
    final = kss.split_sentences(hanspell_sent)
    return final

if __name__ == '__main__':
    model,tok = loadModel()
    sentences = seperate()

    for i in range(len(sentences)):
        predict(sentences[i], model, tok)
