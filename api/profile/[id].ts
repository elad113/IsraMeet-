import type { VercelRequest, VercelResponse } from '@vercel/node';
export default async function handler(req:VercelRequest,res:VercelResponse){
 if(req.method!=='GET'&&req.method!=='PUT')return res.status(405).json({error:'GET or PUT only'});
 const url=process.env.SUPABASE_URL, key=process.env.SUPABASE_SERVICE_ROLE_KEY;
 if(!url||!key)return res.status(503).json({error:'Cloud sync is not configured. Add Supabase server environment variables.'});
 const id=String(req.query.id||'');if(!/^[a-zA-Z0-9_-]{8,100}$/.test(id))return res.status(400).json({error:'Invalid profile id'});
 // This endpoint is intended for app-generated anonymous IDs. Add Supabase Auth before using it for private data.
 if(req.method==='GET'){
  const r=await fetch(url+'/rest/v1/user_data?user_id=eq.'+encodeURIComponent(id)+'&select=data&limit=1',{headers:{apikey:key,Authorization:'Bearer '+key}});
  const rows:any=await r.json();if(!r.ok)return res.status(502).json({error:'Database read failed'});return res.status(200).json({profile:rows?.[0]?.data||null});
 }
 const profile=req.body?.profile;if(!profile||typeof profile!=='object')return res.status(400).json({error:'Missing profile'});
 const r=await fetch(url+'/rest/v1/user_data?on_conflict=user_id',{method:'POST',headers:{apikey:key,Authorization:'Bearer '+key,'Content-Type':'application/json',Prefer:'resolution=merge-duplicates,return=minimal'},body:JSON.stringify({user_id:id,data:profile,updated_at:new Date().toISOString()})});
 if(!r.ok)return res.status(502).json({error:'Database write failed'});return res.status(200).json({ok:true});
}
