import type { VercelRequest, VercelResponse } from '@vercel/node';

function send(res: VercelResponse, status: number, body: unknown) {
  res.status(status).setHeader('Content-Type','application/json; charset=utf-8').send(body);
}
export default async function handler(req: VercelRequest,res: VercelResponse) {
  if(req.method!=='POST') return send(res,405,{error:'POST only'});
  const {messages=[], language='Python', files=[]} = req.body || {};
  if(!Array.isArray(messages) || messages.length===0) return send(res,400,{error:'Add a message first'});
  const system = `You are Codicher, a patient, upbeat programming tutor. Teach from first principles and never assume the learner knows an untaught concept. Explain mistakes kindly. Use the learner's UI language when possible. Current programming language: ${String(language).slice(0,40)}. Attached text context: ${JSON.stringify(files).slice(0,12000)}. If asked to teach, explain briefly, show a small example, then ask one useful practice question. If asked for a quiz, return clear numbered questions. Do not claim to execute code unless a real execution tool was used.`;
  const chatMessages=[{role:'system',content:system},...messages.slice(-24).map((m:any)=>({role:m.role==='assistant'?'assistant':'user',content:String(m.content||'').slice(0,12000)}))];
  const groqKey=process.env.GROQ_API_KEY;
  const geminiKey=process.env.GEMINI_API_KEY;
  const errors:string[]=[];
  if(groqKey){
    try{
      const r=await fetch('https://api.groq.com/openai/v1/chat/completions',{method:'POST',headers:{Authorization:'Bearer '+groqKey,'Content-Type':'application/json'},body:JSON.stringify({model:process.env.GROQ_MODEL||'llama-3.3-70b-versatile',messages:chatMessages,temperature:0.55,max_tokens:1800})});
      const j:any=await r.json(); if(!r.ok) throw new Error(j?.error?.message||'Groq request failed');
      const answer=j?.choices?.[0]?.message?.content; if(!answer) throw new Error('Groq returned an empty answer');
      return send(res,200,{answer,provider:'groq'});
    }catch(e:any){errors.push('Groq: '+(e?.message||'failed'));}
  } else errors.push('Groq key is not configured');
  if(geminiKey){
    try{
      const prompt=chatMessages.map((m:any)=>m.role==='system'?'Tutor instructions: '+m.content:(m.role==='assistant'?'Tutor: ':'Learner: ')+m.content).join('\n\n');
      const r=await fetch('https://generativelanguage.googleapis.com/v1beta/models/'+(process.env.GEMINI_MODEL||'gemini-2.5-flash')+':generateContent?key='+encodeURIComponent(geminiKey),{method:'POST',headers:{'Content-Type':'application/json'},body:JSON.stringify({contents:[{parts:[{text:prompt}]}],generationConfig:{temperature:0.55,maxOutputTokens:1800}})});
      const j:any=await r.json();if(!r.ok)throw new Error(j?.error?.message||'Gemini request failed');
      const answer=j?.candidates?.[0]?.content?.parts?.map((p:any)=>p.text||'').join('');if(!answer)throw new Error('Gemini returned an empty answer');
      return send(res,200,{answer,provider:'gemini'});
    }catch(e:any){errors.push('Gemini: '+(e?.message||'failed'));}
  }else errors.push('Gemini key is not configured');
  return send(res,503,{error:'Codicher could not reach either AI provider.',details:errors});
}
